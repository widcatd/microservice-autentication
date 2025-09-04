package co.com.autentication.model.auth;

import co.com.autentication.model.user.User;

public interface IJwtProvider {
    String generateToken(User user);
}
