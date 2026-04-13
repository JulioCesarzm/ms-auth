package pe.inpe.ms_auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeResponseDTO {
    private Long idUsuario;
    private String username;
}