package co.com.autentication.api;

import co.com.autentication.api.dto.CreateUserDto;
import co.com.autentication.api.exception.ValidatorHandler;
import co.com.autentication.api.exceptionhandler.ControllerAdvisor;
import co.com.autentication.api.helper.IUserRequestMapper;
import co.com.autentication.api.helper.IUserResponseMapper;
import co.com.autentication.usecase.user.api.IUserServicePort;
import co.com.autentication.usecase.user.exception.DataAlreadyExistException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones sobre usuarios")
public class Handler {
    private final IUserServicePort userServicePort;
    private final IUserRequestMapper userRequestMapper;
    private final IUserResponseMapper userResponseMapper;
    private final ValidatorHandler validatorHandler;
    private final ControllerAdvisor controllerAdvisor;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("");
    }
    @Operation(summary = "Crear un usuario")
    public Mono<ServerResponse> saveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateUserDto.class)
                .map(validatorHandler::validate)
                .flatMap(dto -> userServicePort.saveUser(userRequestMapper.toModel(dto)))
                .then(ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(""))
                .onErrorResume(ConstraintViolationException.class,
                        controllerAdvisor::handleConstraintViolation)
                .onErrorResume(DataAlreadyExistException.class, ex ->
                        controllerAdvisor.handleDataAlreadyExistsException(ex, serverRequest));
    }

    public Mono<ServerResponse> listenGETUseCase2(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> findByDocument(ServerRequest serverRequest) {
        String identityDocument = serverRequest.pathVariable("identityDocument");
        return userServicePort.findByDocument(identityDocument)
                .map(userResponseMapper::toResponse)
                .flatMap(userResponseDto ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(userResponseDto)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
