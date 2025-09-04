package co.com.autentication.api.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDto {
    private Long idUser;
    private String firstName;
    private String lastName;
    private LocalDate dateBirthday;
    private String address;
    private String phone;
    private String email;
    private BigDecimal salaryBase;
    private String identityDocument;
}
