package co.com.autentication.api;

import co.com.autentication.api.dto.CreateUserDto;
import co.com.autentication.api.exceptionhandler.ControllerAdvisor;
import co.com.autentication.api.helper.IUserRequestMapper;
import co.com.autentication.api.helper.IUserResponseMapper;
import co.com.autentication.model.constants.Constants;
import co.com.autentication.model.exception.JwtAuthenticationException;
import co.com.autentication.usecase.user.api.IUserServicePort;
import co.com.autentication.usecase.user.exception.DataAlreadyExistException;
import co.com.autentication.usecase.user.exception.UserValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Usuarios", description = "Operaciones sobre usuarios")
public class Handler {
    private final IUserServicePort userServicePort;
    private final IUserRequestMapper userRequestMapper;
    private final IUserResponseMapper userResponseMapper;
    private final ControllerAdvisor controllerAdvisor;

    @Operation(summary = "Crear un usuario")
    public Mono<ServerResponse> saveUser(ServerRequest serverRequest) {
        String traceId = serverRequest.headers().firstHeader("X-Trace-Id");
        log.info(Constants.LOG_USER_REQUEST_RECEIVED, traceId);
        return serverRequest.bodyToMono(CreateUserDto.class)
                .flatMap(dto -> userServicePort.saveUser(userRequestMapper.toModel(dto), traceId))
                .doOnSuccess(user -> log.info(Constants.LOG_USER_CREATED_SUCCESS, traceId))
                .then(ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(""))
                .doOnError(error -> log.error(Constants.LOG_USER_ERROR_PROCESSING, error.getMessage(), traceId, error))
                .onErrorResume(ConstraintViolationException.class,
                        controllerAdvisor::handleConstraintViolation)
                .onErrorResume(DataAlreadyExistException.class, ex ->
                        controllerAdvisor.handleDataAlreadyExistsException(ex, serverRequest))
                .onErrorResume(UserValidationException.class, ex ->
                        controllerAdvisor.handleDataAlreadyExistsException(ex, serverRequest))
                .onErrorResume(JwtAuthenticationException.class, ex ->
                        controllerAdvisor.handleDataAlreadyExistsException(ex, serverRequest));
    }
    @Operation(summary = "Buscar por documento")
    public Mono<ServerResponse> findByDocument(ServerRequest serverRequest) {
        String identityDocument = serverRequest.pathVariable("identityDocument");
        String traceId = serverRequest.headers().firstHeader("X-Trace-Id");
        log.info(Constants.LOG_USER_FIND_BY_DOCUMENT, identityDocument, traceId);
        return userServicePort.findByDocument(identityDocument, traceId)
                .map(userResponseMapper::toResponse)
                .flatMap(userResponseDto ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(userResponseDto)
                )
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnSuccess(resp -> log.info(Constants.LOG_USER_RESPONSE_SENT, resp.statusCode(), traceId))
                .doOnError(error -> log.error(Constants.LOG_USER_ERROR_PROCESSING, error.getMessage(), traceId, error));
    }

    public Mono<ServerResponse> findByEmail(ServerRequest serverRequest) {
        String email = serverRequest.pathVariable("email");
        String traceId = serverRequest.headers().firstHeader("X-Trace-Id");
        log.info(Constants.LOG_USER_FIND_BY_EMAIL, email, traceId);
        return userServicePort.findByEmail(email, traceId)
                .flatMap(user ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(user)
                )
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnSuccess(resp -> log.info(Constants.LOG_USER_RESPONSE_SENT, resp.statusCode(), traceId))
                .doOnError(error -> log.error(Constants.LOG_USER_ERROR_PROCESSING, error.getMessage(), traceId, error));
    }
}
