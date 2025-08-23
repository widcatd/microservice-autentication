package co.com.autentication.model.user.gateways;

import co.com.autentication.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> save(User user);
}
