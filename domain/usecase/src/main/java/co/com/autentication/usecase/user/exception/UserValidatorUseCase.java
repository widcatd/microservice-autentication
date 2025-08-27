package co.com.autentication.usecase.user.exception;

import co.com.autentication.model.user.User;
import co.com.autentication.usecase.user.exceptionusecase.ExceptionUseCaseResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class UserValidatorUseCase {

    public Mono<User> validate(User user) {
        return Mono.just(user)

                .filter(u -> u.getFirstName() != null)
                .switchIfEmpty(Mono.error(new UserValidationException(
                        ExceptionUseCaseResponse.USER_FIRSTNAME_NULL.getCode(),
                        ExceptionUseCaseResponse.USER_FIRSTNAME_NULL.getMessage()
                )))
                .filter(u -> !u.getFirstName().isBlank())
                .switchIfEmpty(Mono.error(new UserValidationException(
                        ExceptionUseCaseResponse.USER_FIRSTNAME_EMPTY.getCode(),
                        ExceptionUseCaseResponse.USER_FIRSTNAME_EMPTY.getMessage()
                )))

                .filter(u -> u.getLastName() != null)
                .switchIfEmpty(Mono.error(new UserValidationException(
                        ExceptionUseCaseResponse.USER_LASTNAME_NULL.getCode(),
                        ExceptionUseCaseResponse.USER_LASTNAME_NULL.getMessage()
                )))
                .filter(u -> !u.getLastName().isBlank())
                .switchIfEmpty(Mono.error(new UserValidationException(
                        ExceptionUseCaseResponse.USER_LASTNAME_EMPTY.getCode(),
                        ExceptionUseCaseResponse.USER_LASTNAME_EMPTY.getMessage()
                )))

                .filter(u -> u.getDateBirthday() == null || !u.getDateBirthday().isAfter(LocalDate.now()))
                .switchIfEmpty(Mono.error(new UserValidationException(
                        ExceptionUseCaseResponse.USER_BIRTHDAY_INVALID.getCode(),
                        ExceptionUseCaseResponse.USER_BIRTHDAY_INVALID.getMessage()
                )))

                .filter(u -> u.getEmail() != null)
                .switchIfEmpty(Mono.error(new UserValidationException(
                        ExceptionUseCaseResponse.USER_EMAIL_NULL.getCode(),
                        ExceptionUseCaseResponse.USER_EMAIL_NULL.getMessage()
                )))
                .filter(u -> !u.getEmail().isBlank())
                .switchIfEmpty(Mono.error(new UserValidationException(
                        ExceptionUseCaseResponse.USER_EMAIL_EMPTY.getCode(),
                        ExceptionUseCaseResponse.USER_EMAIL_EMPTY.getMessage()
                )))
                .filter(u -> u.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$"))
                .switchIfEmpty(Mono.error(new UserValidationException(
                        ExceptionUseCaseResponse.USER_EMAIL_INVALID.getCode(),
                        ExceptionUseCaseResponse.USER_EMAIL_INVALID.getMessage()
                )))

                .filter(u -> u.getSalaryBase() != null)
                .switchIfEmpty(Mono.error(new UserValidationException(
                        ExceptionUseCaseResponse.USER_SALARY_NULL.getCode(),
                        ExceptionUseCaseResponse.USER_SALARY_NULL.getMessage()
                )))
                .filter(u -> u.getSalaryBase().compareTo(BigDecimal.ZERO) >= 0)
                .switchIfEmpty(Mono.error(new UserValidationException(
                        ExceptionUseCaseResponse.USER_SALARY_MIN.getCode(),
                        ExceptionUseCaseResponse.USER_SALARY_MIN.getMessage()
                )))
                .filter(u -> u.getSalaryBase().compareTo(new BigDecimal("15000000.00")) <= 0)
                .switchIfEmpty(Mono.error(new UserValidationException(
                        ExceptionUseCaseResponse.USER_SALARY_MAX.getCode(),
                        ExceptionUseCaseResponse.USER_SALARY_MAX.getMessage()
                )));
    }
}
