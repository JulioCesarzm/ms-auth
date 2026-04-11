package pe.inpe.ms_auth.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateRequestDTO {

    @Email(message = "Email inválido")
    private String email;

    private Long idRol;

    private Long idPersona;
}
