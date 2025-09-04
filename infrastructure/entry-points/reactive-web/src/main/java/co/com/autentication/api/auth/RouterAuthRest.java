package co.com.autentication.api.auth;

import co.com.autentication.api.dto.UserLoginDto;
import co.com.autentication.model.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterAuthRest {
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/auth/signUp",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST,
                    beanClass = AuthHandler.class,
                    beanMethod = "signUp",
                    operation = @Operation(
                            operationId = "signUp",
                            summary = "Registrar un nuevo usuario",
                            tags = { "Autenticación" },
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    description = "Datos del usuario para registrarse",
                                    content = @Content(
                                            schema = @Schema(implementation = User.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Usuario registrado correctamente",
                                            content = @Content(
                                                    schema = @Schema(implementation = User.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error en los datos enviados"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/auth/login",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST,
                    beanClass = AuthHandler.class,
                    beanMethod = "login",
                    operation = @Operation(
                            operationId = "login",
                            summary = "Iniciar sesión de usuario",
                            tags = { "Autenticación" },
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    description = "Credenciales del usuario",
                                    content = @Content(
                                            schema = @Schema(implementation = UserLoginDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Inicio de sesión exitoso",
                                            content = @Content(
                                                    schema = @Schema(implementation = UserLoginDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "Credenciales inválidas"
                                    )
                            }
                    )
            )
    })
    @Bean
    public RouterFunction<ServerResponse> authRouterFunction(AuthHandler handler) {
        return route(POST("/api/v1/auth/login"),handler::login)
                .andRoute(POST("/api/v1/auth/signUp"), handler::signUp);
    }
}
