package co.com.autentication.model.exceptionusecase;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionUseCaseResponse {
    INTERNAL_SERVER_ERROR("ERR-500", "error.internal.server"),
    NO_DATA_FOUND("ERR-001", "No data found for the requested petition"),
    VALIDATION_ERROR("ERR-003", "Validation failed for the request"),
    DATA_ALREADY_EXISTS("ERR-004","The program with ID %d already has an assigned user"),
    ID_USER_NOT_FOUND("ERR-006","ID_USER %d not found"),
    EMAIL_ALREADY_EXIST("ERR-007","El email %s ya se encuentra registrado"),
    ID_USER_ALREADY_EXISTS("ERR-008", "The program already has an assigned ID_USER %d"),
    USER_FIRSTNAME_NULL("USR-001", "el nombre no debe ser nulo"),
    USER_FIRSTNAME_EMPTY("USR-002", "el nombre no debe estar vacío"),
    USER_LASTNAME_NULL("USR-003", "el apellido no debe ser nulo"),
    USER_LASTNAME_EMPTY("USR-004", "el apellido no debe estar vacío"),
    USER_BIRTHDAY_INVALID("USR-005", "la fecha de nacimiento no puede ser futura"),
    USER_EMAIL_NULL("USR-006", "el email no debe ser nulo"),
    USER_EMAIL_EMPTY("USR-007", "el email no debe estar vacío"),
    USER_EMAIL_INVALID("USR-008", "el formato de email es incorrecto"),
    USER_SALARY_NULL("USR-009", "el salario base no debe ser nulo"),
    USER_SALARY_MIN("USR-010", "el valor mínimo del salario base debe ser 0"),
    USER_SALARY_MAX("USR-011", "el valor máximo del salario base debe ser 15000000"),

    ROLE_ID_NOT_FOUND("ROL-001","No existe el id solicitud prestamo %d"),

    EMAIL_NOT_FOUND("AUTH-001", "El email %s no se encuentra registrado"),
    INVALID_CREDENTIALS("AUTH-002", "Las credenciales son inválidas, verifique su email o contraseña"),

    JWT_TOKEN_NOT_FOUND("JWT-001", "El token no se encontró en la cabecera Authorization"),
    JWT_TOKEN_INVALID("JWT-002", "El token enviado es inválido"),
    JWT_TOKEN_EXPIRED("JWT-003", "El token ha expirado"),
    JWT_TOKEN_UNSUPPORTED("JWT-004", "El formato del token no es soportado"),
    JWT_TOKEN_MALFORMED("JWT-005", "El token está mal formado"),
    JWT_UNAUTHORIZED("JWT-006", "No autorizado: se requiere un token válido para acceder a este recurso");

    private final String code;
    private final String message;
}
