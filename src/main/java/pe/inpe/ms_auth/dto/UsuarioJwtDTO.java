package pe.inpe.ms_auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioJwtDTO {

    private Long idUsuario;
    private String username;
    private String email;
    private String rol;
    private String token;
    private String type = "Bearer";
    private Long expiresIn;
}
