package co.com.autentication.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserLoginDto {
    @Schema(description = "Correo del usuario", example = "juanito3@example.com")
    private String email;
    @Schema(description = "Contrasena del usuario", example = "prueba123")
    private String password;
}
