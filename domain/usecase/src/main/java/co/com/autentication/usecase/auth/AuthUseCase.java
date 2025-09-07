package co.com.autentication.usecase.auth;

import co.com.autentication.model.auth.IJwtProvider;
import co.com.autentication.model.auth.IPasswordEncoder;
import co.com.autentication.model.exception.DataNotFoundException;
import co.com.autentication.model.exception.InvalidCredentialsException;
import co.com.autentication.model.exceptionusecase.ExceptionUseCaseResponse;
import co.com.autentication.model.user.User;
import co.com.autentication.model.user.UserDetails;
import co.com.autentication.model.user.UserLogin;
import co.com.autentication.model.user.gateways.UserRepository;
import co.com.autentication.usecase.user.exception.DataAlreadyExistException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthUseCase {
    private final UserRepository userRepository;
    private final IJwtProvider jwtProvider;
    private final IPasswordEncoder passwordEncoder;

    public Mono<User> singUp(User user, String traceId) {
        return  userRepository.findByEmail(user.getEmail(),traceId)
                .flatMap(existingUser -> Mono.<User>error(new DataAlreadyExistException(
                        ExceptionUseCaseResponse.EMAIL_ALREADY_EXIST.getCode(),
                        String.format(ExceptionUseCaseResponse.EMAIL_ALREADY_EXIST.getMessage(), existingUser.getEmail())
                )))
                .switchIfEmpty(Mono.defer(() -> {
                    user.setPassword(passwordEncoder.hash(user.getPassword()));
                    return userRepository.save(user, traceId);
                }));
    }
    public Mono<UserDetails> login(UserLogin userLogin, String traceId) {
        return userRepository.findByEmail(userLogin.getEmail(), traceId)
                .switchIfEmpty(Mono.error(new DataNotFoundException(
                        ExceptionUseCaseResponse.EMAIL_NOT_FOUND.getCode(),
                        String.format(ExceptionUseCaseResponse.EMAIL_NOT_FOUND.getMessage(), userLogin.getEmail())
                )))
                .flatMap(user -> {
                    if (!passwordEncoder.matches(userLogin.getPassword(), user.getPassword())) {
                        return Mono.error(new InvalidCredentialsException(
                                ExceptionUseCaseResponse.INVALID_CREDENTIALS.getCode(),
                                ExceptionUseCaseResponse.INVALID_CREDENTIALS.getMessage()
                        ));
                    }
                    String token = jwtProvider.generateToken(user);
                    UserDetails userDetails = UserDetails.builder()
                            .idUser(user.getIdUser())
                            .email(user.getEmail())
                            .role(user.getRole())
                            .token(token)
                            .build();
                    return Mono.just(userDetails);
                });
    }
}
