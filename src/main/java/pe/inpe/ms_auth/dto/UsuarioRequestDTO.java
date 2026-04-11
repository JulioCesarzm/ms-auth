package pe.inpe.ms_auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "El password es obligatorio")
    @Size(min = 8, message = "El password debe tener al menos 8 caracteres")
    private String password;

    @Email(message = "Email inválido")
    private String email;

    @NotNull(message = "El ID del rol es obligatorio")
    private Long idRol;

    private Long idPersona;
}