package pe.inpe.ms_auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long idUsuario;
    private Long idPersona;
    private String username;
    private String email;
    private String rol;
    private Boolean estado;
    private LocalDateTime ultimoAcceso;
}