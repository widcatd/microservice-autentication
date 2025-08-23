package co.com.autentication.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateUserDto {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String dateBirthday;
    private String direction;
    private String phone;
    @NotNull
    @Email
    private String email;
    private Long salaryBase;
}
