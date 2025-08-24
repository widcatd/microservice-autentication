package co.com.autentication.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table(name = "usuario")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {
    @Id
    @Column("id_usuario")
    private Long idUser;
    @Column("nombre")
    private String firstName;
    @Column("apellido")
    private String lastName;
    @Column("fecha_nacimiento")
    private LocalDate dateBirthday;
    @Column("direccion")
    private String address;
    @Column("telefono")
    private String phone;
    @Column("email")
    private String email;
    @Column("salario_base")
    private Long salaryBase;
}
