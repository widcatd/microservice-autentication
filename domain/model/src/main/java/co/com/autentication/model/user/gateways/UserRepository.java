package co.com.autentication.model.user.gateways;

import co.com.autentication.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> save(User user, String traceId);
    Mono<User> findByEmail(String email,String traceId);
    Mono<User> findByIdentityDocument(String identityDocument, String traceId);
    Mono<User> signUp(User user);
}
