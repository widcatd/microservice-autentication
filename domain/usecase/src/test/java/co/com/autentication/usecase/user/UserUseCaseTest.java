package co.com.autentication.usecase.user;

import co.com.autentication.model.user.User;
import co.com.autentication.model.user.gateways.UserRepository;
import co.com.autentication.usecase.user.mock.UserMock;
import co.com.autentication.usecase.user.exception.UserValidationException;
import co.com.autentication.usecase.user.exception.UserValidatorUseCase;
import co.com.autentication.model.exceptionusecase.ExceptionUseCaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;


class UserUseCaseTest {
    private static final String IDENTITY_DOCUMENT ="123456";
    private static final String traceId= "74654859-d75f-448e-9b5e-a6ddee9f5278";

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidatorUseCase userValidator;

    @InjectMocks
    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Usuario guardado exitosamente")
    void saveUser_successful() {
        User user = UserMock.validUser();
        when(userValidator.validate(user)).thenReturn(Mono.just(user));
        when(userRepository.findByEmail(user.getEmail(),traceId)).thenReturn(Mono.empty());
        when(userRepository.save(user,traceId)).thenReturn(Mono.just(user));

        Mono<Void> result = userUseCase.saveUser(user,traceId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(userRepository).save(user,traceId);
    }
    @Test
    @DisplayName("Error al validar usuario")
    void saveUser_validationFails() {
        User user = UserMock.invalidUser();
        when(userValidator.validate(user))
                .thenReturn(Mono.error(new UserValidationException(
                        ExceptionUseCaseResponse.VALIDATION_ERROR.getCode(),
                        ExceptionUseCaseResponse.VALIDATION_ERROR.getMessage()
                )));

        Mono<Void> result = userUseCase.saveUser(user, traceId);

        StepVerifier.create(result)
                .expectError(UserValidationException.class)
                .verify();

        verify(userRepository, never()).save(any(), eq(traceId));
        verify(userRepository, never()).findByEmail(anyString(), anyString());
    }
    @Test
    @DisplayName("Usuario encontrado por documento")
    void findByDocument_found() {
        User user = UserMock.validUser();
        when(userRepository.findByIdentityDocument(IDENTITY_DOCUMENT,traceId))
                .thenReturn(Mono.just(user));

        Mono<User> result = userUseCase.findByDocument(IDENTITY_DOCUMENT, traceId);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("Usuario no encontrado por documento")
    void findByDocument_notFound() {
        when(userRepository.findByIdentityDocument(IDENTITY_DOCUMENT,traceId))
                .thenReturn(Mono.empty());

        Mono<User> result = userUseCase.findByDocument(IDENTITY_DOCUMENT, traceId);

        StepVerifier.create(result)
                .verifyComplete();
    }
}