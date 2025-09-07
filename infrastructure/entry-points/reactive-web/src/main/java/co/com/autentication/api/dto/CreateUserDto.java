package co.com.autentication.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateUserDto {
    @Schema(description = "Nombre completo del usuario", example = "Juan Jesus")
    private String firstName;
    @Schema(description = "Apellido completo del usuario", example = "Perez Dominguez")
    private String lastName;
    @Schema(description = "Fecha de nacimiento", example = "1990-05-15")
    private LocalDate dateBirthday;
    @Schema(description = "Dirección del usuario", example = "Av. Siempre Viva 123")
    private String address;
    @Schema(description = "Teléfono de contacto", example = "+51 987654321")
    private String phone;
    @Schema(description = "Email del usuario", example = "juan.perez@example.com")
    private String email;
    @Schema(description = "Salario base", example = "2500.50")
    private BigDecimal salaryBase;
    private String identityDocument;
    private Long idRole;
    private String password;
}
