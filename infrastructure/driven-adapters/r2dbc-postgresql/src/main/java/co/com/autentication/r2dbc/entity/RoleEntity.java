package co.com.autentication.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "rol")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoleEntity {
    @Id
    @Column("id_role")
    private Long idRole;
    @Column("nombre")
    private String name;
    @Column("descripcion")
    private String description;
}
