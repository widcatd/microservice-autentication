package co.com.autentication.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {
    @Id
    @Column("id_user")
    private Long idUser;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    @Column("birth_date")
    private String birthDate;
    @Column("address")
    private String address;
    @Column("phone")
    private String phone;
    private String email;
    @Column("salaty_base")
    private Long salaryBase;
}
