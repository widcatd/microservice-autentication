package co.com.autentication.usecase.user.exception;

import lombok.Getter;

@Getter
public class UserValidationException extends RuntimeException {
  private final String code;
  private final String message;

  public UserValidationException(String code, String message) {
    super(message);
    this.code = code;
    this.message = message;
  }
}
