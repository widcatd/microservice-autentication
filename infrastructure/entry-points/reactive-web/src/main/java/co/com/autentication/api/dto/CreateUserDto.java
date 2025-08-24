package co.com.autentication.api.dto;

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
    @NotNull(message = "el nombre no debe ser nulo")
    @NotEmpty(message = "el nombre no debe estar vacio")
    private String firstName;
    @NotNull(message = "el apellido no debe ser nulo")
    @NotEmpty(message = "el apellido no debe estar vacio")
    private String lastName;
    private LocalDate dateBirthday;
    private String address;
    private String phone;
    @NotNull(message = "el email no debe ser nulo")
    @Email(message = "el formato de email es incorrecto")
    @NotEmpty(message = "el email no debe estar vacio")
    private String email;
    @NotNull(message = "el saiario base no debe ser nulo")
    @DecimalMin(value = "0.00", message = "el valor minimo del salario base debe ser 0")
    @DecimalMax(value = "15000000.00", message = "el valor maximo del salario base debe ser 15000000")
    private BigDecimal salaryBase;
}
