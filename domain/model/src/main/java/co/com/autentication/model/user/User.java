package co.com.autentication.model.user;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long idUser;
    private String firstName;
    private String lastName;
    private String dateBirthday;
    private String direction;
    private String phone;
    private String email;
    private Long salaryBase;
}
