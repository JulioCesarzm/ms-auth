package pe.inpe.ms_auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.inpe.ms_auth.util.auditoria.AuditModel;

@Entity
@Table(name = "rol_sistema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolSistema extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estado")
    private Boolean estado = true;
}
