package co.com.autentication.model.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserLogin {
    private String email;
    private String password;
}
