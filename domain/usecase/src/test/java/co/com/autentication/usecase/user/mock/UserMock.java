package co.com.autentication.usecase.user.mock;

import co.com.autentication.model.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UserMock {
    public static User validUser() {
        return User.builder()
                .idUser(1L)
                .firstName("Juan")
                .lastName("Pérez")
                .dateBirthday(LocalDate.of(1990, 5, 20))
                .address("Av. Siempre Viva 123")
                .phone("987654321")
                .email("juan.perez@mail.com")
                .salaryBase(BigDecimal.valueOf(3500.00))
                .identityDocument("12345678")
                .build();
    }

    public static User invalidUser() {
        return User.builder()
                .idUser(2L)
                .firstName("") // inválido
                .lastName(null) // inválido
                .dateBirthday(null)
                .address("")
                .phone("")
                .email("") // inválido
                .salaryBase(BigDecimal.ZERO)
                .identityDocument(null) // inválido
                .build();
    }

    public static User duplicatedUser() {
        return User.builder()
                .idUser(3L)
                .firstName("Juan")
                .lastName("Pérez")
                .dateBirthday(LocalDate.of(1990, 5, 20))
                .address("Av. Siempre Viva 123")
                .phone("987654321")
                .email("juan.perez@mail.com") // mismo correo que validUser()
                .salaryBase(BigDecimal.valueOf(3500.00))
                .identityDocument("12345678") // mismo doc que validUser()
                .build();
    }
}
