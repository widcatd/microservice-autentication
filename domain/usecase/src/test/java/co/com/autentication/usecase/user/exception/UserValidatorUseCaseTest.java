package co.com.autentication.usecase.user.exception;

import co.com.autentication.model.user.User;
import co.com.autentication.usecase.user.mock.UserMock;
import co.com.autentication.usecase.user.exceptionusecase.ExceptionUseCaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

class UserValidatorUseCaseTest {
    public static final String EMPTY = "   ";
    public static final String INVALID_EMAIL = "invalid-email";
    public static final BigDecimal SALARY_NEGATIVE = BigDecimal.valueOf(-10);
    public static final BigDecimal EXCEEDS_MAX_SALARY = new BigDecimal("20000000.00");

    @InjectMocks
    private UserValidatorUseCase validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Usuario validado exitosamente")
    void shouldValidateSuccessfully() {
        User user = UserMock.validUser();

        Mono<User> result = validator.validate(user);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("Falla por nombre nulo")
    void shouldFailWhenFirstNameIsNull() {
        User user = UserMock.validUser();
        user.setFirstName(null);

        Mono<User> result = validator.validate(user);

        StepVerifier.create(result)
                .expectErrorSatisfies(error -> {
                    assert error instanceof UserValidationException;
                    UserValidationException ex = (UserValidationException) error;
                    assert ex.getCode().equals(ExceptionUseCaseResponse.USER_FIRSTNAME_NULL.getCode());
                })
                .verify();
    }

    @Test
    @DisplayName("Falla por nombre vacío")
    void shouldFailWhenFirstNameIsEmpty() {
        User user = UserMock.validUser();
        user.setFirstName(EMPTY);

        Mono<User> result = validator.validate(user);

        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof UserValidationException &&
                        ((UserValidationException) error).getCode().equals(ExceptionUseCaseResponse.USER_FIRSTNAME_EMPTY.getCode()))
                .verify();
    }

    @Test
    @DisplayName("Falla por apellido nulo")
    void shouldFailWhenLastNameIsNull() {

        User user = UserMock.validUser();
        user.setLastName(null);

        Mono<User> result = validator.validate(user);

        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof UserValidationException &&
                        ((UserValidationException) error).getCode().equals(ExceptionUseCaseResponse.USER_LASTNAME_NULL.getCode()))
                .verify();
    }

    @Test
    @DisplayName("Falla por apellido vacío")
    void shouldFailWhenLastNameIsEmpty() {
        User user = UserMock.validUser();
        user.setLastName(EMPTY);

        Mono<User> result = validator.validate(user);

        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof UserValidationException &&
                        ((UserValidationException) error).getCode().equals(ExceptionUseCaseResponse.USER_LASTNAME_EMPTY.getCode()))
                .verify();
    }

    @Test
    @DisplayName("Falla por fecha de nacimiento futura")
    void shouldFailWhenBirthdayIsInFuture() {
        User user = UserMock.validUser();
        user.setDateBirthday(LocalDate.now().plusDays(1));

        Mono<User> result = validator.validate(user);

        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof UserValidationException &&
                        ((UserValidationException) error).getCode().equals(ExceptionUseCaseResponse.USER_BIRTHDAY_INVALID.getCode()))
                .verify();
    }

    @Test
    @DisplayName("Falla por correo nulo")
    void shouldFailWhenEmailIsNull() {
        User user = UserMock.validUser();
        user.setEmail(null);

        Mono<User> result = validator.validate(user);

        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof UserValidationException &&
                        ((UserValidationException) error).getCode().equals(ExceptionUseCaseResponse.USER_EMAIL_NULL.getCode()))
                .verify();
    }

    @Test
    @DisplayName("Falla por correo vacío")
    void shouldFailWhenEmailIsEmpty() {
        User user = UserMock.validUser();
        user.setEmail(EMPTY);

        Mono<User> result = validator.validate(user);

        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof UserValidationException &&
                        ((UserValidationException) error).getCode().equals(ExceptionUseCaseResponse.USER_EMAIL_EMPTY.getCode()))
                .verify();
    }

    @Test
    @DisplayName("Falla por correo inválido")
    void shouldFailWhenEmailIsInvalid() {
        User user = UserMock.validUser();
        user.setEmail(INVALID_EMAIL);

        Mono<User> result = validator.validate(user);

        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof UserValidationException &&
                        ((UserValidationException) error).getCode().equals(ExceptionUseCaseResponse.USER_EMAIL_INVALID.getCode()))
                .verify();
    }

    @Test
    @DisplayName("Falla por salario nulo")
    void shouldFailWhenSalaryIsNull() {
        User user = UserMock.validUser();
        user.setSalaryBase(null);

        Mono<User> result = validator.validate(user);

        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof UserValidationException &&
                        ((UserValidationException) error).getCode().equals(ExceptionUseCaseResponse.USER_SALARY_NULL.getCode()))
                .verify();
    }

    @Test
    @DisplayName("Falla por salario negativo")
    void shouldFailWhenSalaryIsNegative() {
        User user = UserMock.validUser();
        user.setSalaryBase(SALARY_NEGATIVE);

        Mono<User> result = validator.validate(user);

        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof UserValidationException &&
                        ((UserValidationException) error).getCode().equals(ExceptionUseCaseResponse.USER_SALARY_MIN.getCode()))
                .verify();
    }

    @Test
    @DisplayName("Falla por salario mayor al máximo")
    void shouldFailWhenSalaryExceedsMax() {
        User user = UserMock.validUser();
        user.setSalaryBase(EXCEEDS_MAX_SALARY);

        Mono<User> result = validator.validate(user);

        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof UserValidationException &&
                        ((UserValidationException) error).getCode().equals(ExceptionUseCaseResponse.USER_SALARY_MAX.getCode()))
                .verify();
    }
}