package co.com.autentication.usecase.auth;

import co.com.autentication.model.auth.IJwtProvider;
import co.com.autentication.model.auth.IPasswordEncoder;
import co.com.autentication.model.exception.DataNotFoundException;
import co.com.autentication.model.exception.InvalidCredentialsException;
import co.com.autentication.model.user.User;
import co.com.autentication.model.user.UserDetails;
import co.com.autentication.model.user.UserLogin;
import co.com.autentication.model.user.gateways.UserRepository;
import co.com.autentication.usecase.user.exception.DataAlreadyExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

class AuthUseCaseTest {

    private static final String TRACE_ID = "1234-trace";
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "plainPass";
    private static final String WRONG_PASSWORD = "wrongPass";
    private static final String HASHED_PASS = "hashedPass";
    private static final String ROLE = "ADMIN";
    private static final String TOKEN = "jwt-token";
    private static final Long USER_ID = 1L;
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String ADDRESS = "Main Street 123";
    private static final String PHONE = "987654321";
    private static final String IDENTITY_DOCUMENT = "123456789";
    private static final Long ROLE_ID = 2L;
    private static final BigDecimal SALARY = BigDecimal.valueOf(2500.00);
    private static final LocalDate BIRTHDAY = LocalDate.of(1990, 1, 1);

    @Mock
    private UserRepository userRepository;

    @Mock
    private IJwtProvider jwtProvider;

    @Mock
    private IPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthUseCase authUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("SignUp falla cuando el email ya existe")
    void signUp_shouldFail_whenEmailExists() {
        User existingUser = User.builder()
                .idUser(USER_ID)
                .email(EMAIL)
                .build();

        when(userRepository.findByEmail(EMAIL, TRACE_ID)).thenReturn(Mono.just(existingUser));

        StepVerifier.create(authUseCase.singUp(existingUser, TRACE_ID))
                .expectError(DataAlreadyExistException.class)
                .verify();

        verify(userRepository).findByEmail(EMAIL, TRACE_ID);
        verify(userRepository, never()).save(any(), any());
    }

    @Test
    @DisplayName("SignUp exitoso cuando el usuario es nuevo")
    void signUp_shouldSave_whenUserNotExists() {
        User newUser = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .dateBirthday(BIRTHDAY)
                .address(ADDRESS)
                .phone(PHONE)
                .email(EMAIL)
                .salaryBase(SALARY)
                .identityDocument(IDENTITY_DOCUMENT)
                .idRole(ROLE_ID)
                .password(PASSWORD)
                .build();

        when(userRepository.findByEmail(EMAIL, TRACE_ID)).thenReturn(Mono.empty());
        when(passwordEncoder.hash(PASSWORD)).thenReturn(HASHED_PASS);
        when(userRepository.save(any(User.class), eq(TRACE_ID)))
                .thenAnswer(invocation -> {
                    User saved = invocation.getArgument(0);
                    saved.setIdUser(USER_ID);
                    return Mono.just(saved);
                });

        StepVerifier.create(authUseCase.singUp(newUser, TRACE_ID))
                .assertNext(user -> {
                    assert user.getIdUser().equals(USER_ID);
                    assert user.getPassword().equals(HASHED_PASS);
                })
                .verifyComplete();

        verify(userRepository).save(any(User.class), eq(TRACE_ID));
    }

    @Test
    @DisplayName("Login falla cuando el email no existe")
    void login_shouldFail_whenUserNotFound() {
        UserLogin login = new UserLogin(EMAIL, PASSWORD);

        when(userRepository.findByEmail(EMAIL, TRACE_ID)).thenReturn(Mono.empty());

        StepVerifier.create(authUseCase.login(login, TRACE_ID))
                .expectError(DataNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Login falla cuando la contraseña es incorrecta")
    void login_shouldFail_whenPasswordInvalid() {
        UserLogin login = new UserLogin(EMAIL, WRONG_PASSWORD);

        User user = User.builder()
                .idUser(USER_ID)
                .email(EMAIL)
                .password(HASHED_PASS)
                .build();

        when(userRepository.findByEmail(EMAIL, TRACE_ID)).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(WRONG_PASSWORD, HASHED_PASS)).thenReturn(false);

        StepVerifier.create(authUseCase.login(login, TRACE_ID))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }

    @Test
    @DisplayName("Login exitoso con credenciales correctas")
    void login_shouldSucceed_whenCredentialsValid() {
        UserLogin login = new UserLogin(EMAIL, PASSWORD);

        User user = User.builder()
                .idUser(USER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(HASHED_PASS)
                .role(ROLE)
                .build();

        when(userRepository.findByEmail(EMAIL, TRACE_ID)).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(PASSWORD, HASHED_PASS)).thenReturn(true);
        when(jwtProvider.generateToken(user)).thenReturn(TOKEN);

        StepVerifier.create(authUseCase.login(login, TRACE_ID))
                .assertNext(details -> {
                    assert details instanceof UserDetails;
                    assert details.getIdUser().equals(USER_ID);
                    assert details.getEmail().equals(EMAIL);
                    assert details.getRole().equals(ROLE);
                    assert details.getToken().equals(TOKEN);
                })
                .verifyComplete();
    }
}