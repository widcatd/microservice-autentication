package co.com.autentication.model.auth;

public interface IPasswordEncoder {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
