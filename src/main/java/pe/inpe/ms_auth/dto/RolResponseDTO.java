package pe.inpe.ms_auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolResponseDTO {

    private Long idRol;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Boolean estado;
}