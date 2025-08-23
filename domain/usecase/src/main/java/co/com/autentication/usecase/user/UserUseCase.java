package co.com.autentication.usecase.user;

import co.com.autentication.model.user.User;
import co.com.autentication.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        return userRepository.save(user);
    }
}
