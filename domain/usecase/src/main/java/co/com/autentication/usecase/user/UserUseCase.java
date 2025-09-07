package co.com.autentication.usecase.user;

import co.com.autentication.model.user.User;
import co.com.autentication.model.user.gateways.UserRepository;
import co.com.autentication.usecase.user.api.IUserServicePort;
import co.com.autentication.usecase.user.exception.DataAlreadyExistException;
import co.com.autentication.usecase.user.exception.UserValidatorUseCase;
import co.com.autentication.model.exceptionusecase.ExceptionUseCaseResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {
    private final UserRepository userRepository;
    private final UserValidatorUseCase userValidatorUseCase;

    public Mono<Void> saveUser(User user, String traceId) {
        return userValidatorUseCase.validate(user)
                .flatMap(validUser ->
                        userRepository.findByEmail(validUser.getEmail(), traceId)
                                .flatMap(existingUser -> Mono.error(new DataAlreadyExistException(
                                        ExceptionUseCaseResponse.EMAIL_ALREADY_EXIST.getCode(),
                                        String.format(ExceptionUseCaseResponse.EMAIL_ALREADY_EXIST.getMessage(), existingUser.getEmail())
                                )))
                                .switchIfEmpty(userRepository.save(validUser, traceId))
                )
                .then();
    }

    @Override
    public Mono<User> findByDocument(String identityDocument, String traceId) {
        return userRepository.findByIdentityDocument(identityDocument, traceId);
    }

    @Override
    public Mono<User> findByEmail(String email, String traceId) {
        return userRepository.findByEmail(email, traceId);
    }
}
