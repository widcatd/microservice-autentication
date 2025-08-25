package co.com.autentication.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateUserDto {
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    @NotNull(message = "el nombre no debe ser nulo")
    @NotEmpty(message = "el nombre no debe estar vacio")
    private String firstName;
    @NotNull(message = "el apellido no debe ser nulo")
    @NotEmpty(message = "el apellido no debe estar vacio")
    private String lastName;
    @Schema(description = "Fecha de nacimiento", example = "1990-05-15")
    private LocalDate dateBirthday;
    @Schema(description = "Dirección del usuario", example = "Av. Siempre Viva 123")
    private String address;
    @Schema(description = "Teléfono de contacto", example = "+51 987654321")
    private String phone;
    @NotNull(message = "el email no debe ser nulo")
    @NotEmpty(message = "el email no debe estar vacio")
    @Email(message = "el formato de email es incorrecto")
    @Schema(description = "Email del usuario", example = "juan.perez@example.com")
    private String email;
    @NotNull(message = "el salario base no debe ser nulo")
    @DecimalMin(value = "0.00", message = "el valor minimo del salario base debe ser 0")
    @DecimalMax(value = "15000000.00", message = "el valor maximo del salario base debe ser 15000000")
    @Schema(description = "Salario base", example = "2500.50")
    private BigDecimal salaryBase;
    private String identityDocument;
}
