package co.com.autentication.api.exceptionhandler;

import co.com.autentication.usecase.user.exception.DataAlreadyExistException;
import co.com.autentication.usecase.user.exception.UserValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ControllerAdvisor {
    public Mono<ServerResponse> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> details = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                ExceptionResponse.VALIDATION_ERROR.getCode(),
                ExceptionResponse.VALIDATION_ERROR.getMessage(),
                details
        );

        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }
    public Mono<ServerResponse> handleDataAlreadyExistsException(DataAlreadyExistException ex, ServerRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT,
                ex.getCode(),
                ex.getMessage(),
                request.path()
        );

        return ServerResponse.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }
    public Mono<ServerResponse> handleDataAlreadyExistsException(UserValidationException ex, ServerRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getCode(),
                ex.getMessage(),
                request.path()
        );

        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }
}
