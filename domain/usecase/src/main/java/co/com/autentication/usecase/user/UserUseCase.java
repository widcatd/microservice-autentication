package co.com.autentication.usecase.user;

import co.com.autentication.model.user.User;
import co.com.autentication.model.user.gateways.UserRepository;
import co.com.autentication.usecase.user.api.IUserServicePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {
    private final UserRepository userRepository;

    public Mono<Void> saveUser(User user) {
        return userRepository.save(user).then();
    }
}
