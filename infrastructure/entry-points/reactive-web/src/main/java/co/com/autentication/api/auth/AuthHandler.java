package co.com.autentication.api.auth;

import co.com.autentication.api.exceptionhandler.ControllerAdvisor;
import co.com.autentication.model.constants.Constants;
import co.com.autentication.model.exception.DataNotFoundException;
import co.com.autentication.model.exception.InvalidCredentialsException;
import co.com.autentication.model.exception.JwtAuthenticationException;
import co.com.autentication.model.user.User;
import co.com.autentication.model.user.UserLogin;
import co.com.autentication.usecase.auth.AuthUseCase;
import co.com.autentication.usecase.user.api.IUserServicePort;
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
public class AuthHandler {
    private final AuthUseCase authUseCase;
    private final ControllerAdvisor controllerAdvisor;

    public Mono<ServerResponse> hello(ServerRequest request) {

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(Mono.just("Hello"), String.class);
    }
    public Mono<ServerResponse> signUp(ServerRequest serverRequest) {
        String traceId = serverRequest.headers().firstHeader("X-Trace-Id");
        log.info(Constants.LOG_USER_REQUEST_RECEIVED, traceId);

        return serverRequest.bodyToMono(User.class)
                .flatMap(user -> authUseCase.singUp(user, traceId))
                .flatMap(userLogin -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userLogin));
    }
    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        String traceId = serverRequest.headers().firstHeader("X-Trace-Id");
        log.info(Constants.LOG_LOGIN_REQUEST_RECEIVED, traceId);
        return serverRequest.bodyToMono(UserLogin.class)
                .flatMap(user -> authUseCase.login(user, traceId))
                .flatMap(userLogin -> {
                    log.info(Constants.LOG_LOGIN_SUCCESS, userLogin.getEmail(), traceId);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(userLogin);
                })
                .doOnError(error ->
                        log.error(Constants.LOG_LOGIN_ERROR, error.getMessage(), traceId))
                .onErrorResume(DataNotFoundException.class, ex ->
                        controllerAdvisor.handleDataAlreadyExistsException(ex, serverRequest))
                .onErrorResume(InvalidCredentialsException.class, ex ->
                        controllerAdvisor.handleDataAlreadyExistsException(ex, serverRequest));

    }

}
