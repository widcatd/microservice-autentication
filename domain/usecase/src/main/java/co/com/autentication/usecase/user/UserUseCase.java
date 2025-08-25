package co.com.autentication.usecase.user;

import co.com.autentication.model.user.User;
import co.com.autentication.model.user.gateways.UserRepository;
import co.com.autentication.usecase.user.api.IUserServicePort;
import co.com.autentication.usecase.user.exception.DataAlreadyExistException;
import co.com.autentication.usecase.user.exceptionusecase.ExceptionUseCaseResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {
    private final UserRepository userRepository;

    public Mono<Void> saveUser(User user) {
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existingUser -> Mono.error(new DataAlreadyExistException(
                                ExceptionUseCaseResponse.EMAIL_ALREADY_EXIST.getCode(),
                                String.format(ExceptionUseCaseResponse.EMAIL_ALREADY_EXIST.getMessage(), existingUser.getEmail())))
                .switchIfEmpty(userRepository.save(user)))
                .then();
    }

    @Override
    public Mono<User> findByDocument(String identityDocument) {
        return userRepository.findByIdentityDocument(identityDocument);
    }
}
