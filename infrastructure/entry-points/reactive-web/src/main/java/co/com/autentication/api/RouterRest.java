package co.com.autentication.api;

import co.com.autentication.api.dto.CreateUserDto;
import co.com.autentication.api.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios/",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "saveUser",
                    operation = @Operation(
                            operationId = "saveUser",
                            summary = "Crear un usuario",
                            tags = { "Usuarios" },
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    description = "Datos de creación del usuario",
                                    content = @Content(
                                            schema = @Schema(implementation = CreateUserDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Usuario creado correctamente",
                                            content = @Content(
                                                    schema = @Schema(implementation = CreateUserDto.class)
                                            )
                                    ),
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/findByDocument/{identityDocument}",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "findByDocument",
                    operation = @Operation(
                            operationId = "findByDocument",
                            summary = "Buscar un usuario por documento de identidad",
                            tags = { "Usuarios" },
                            parameters = {
                                    @Parameter(
                                            name = "identityDocument",
                                            in = ParameterIn.PATH,
                                            required = true,
                                            description = "Documento de identidad del usuario"
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Usuario encontrado",
                                            content = @Content(
                                                    schema = @Schema(implementation = UserDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Usuario no encontrado"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/findByEmail/{email}",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "findByEmail",
                    operation = @Operation(
                            operationId = "findByEmail",
                            summary = "Buscar un usuario por email",
                            tags = { "Usuarios" },
                            parameters = {
                                    @Parameter(
                                            name = "email",
                                            in = ParameterIn.PATH,
                                            required = true,
                                            description = "Email del usuario",
                                            example = "usuario@dominio.com"
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Usuario encontrado",
                                            content = @Content(
                                                    schema = @Schema(implementation = UserDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Usuario no encontrado"
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/v1/usuarios/findByDocument/{identityDocument}"),handler::findByDocument)
                .andRoute(POST("/api/v1/usuarios/"), handler::saveUser)
                .andRoute(GET("/api/v1/usuarios/findByEmail/{email}"), handler::findByEmail);
    }
}
