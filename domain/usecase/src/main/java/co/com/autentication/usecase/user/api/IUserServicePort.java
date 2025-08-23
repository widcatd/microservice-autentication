package co.com.autentication.usecase.user.api;

import co.com.autentication.model.user.User;
import reactor.core.publisher.Mono;

public interface IUserServicePort {
    Mono<Void> saveUser(User user);
}
