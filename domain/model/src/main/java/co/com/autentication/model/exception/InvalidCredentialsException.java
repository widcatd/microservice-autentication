package co.com.autentication.model.exception;

import lombok.Getter;

@Getter
public class InvalidCredentialsException extends RuntimeException {
    private final String code;
    private final String message;

    public InvalidCredentialsException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
