package co.com.autentication.model.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDetails {
    private Long idUser;
    private String email;
    private String role;
    private String token;
}
