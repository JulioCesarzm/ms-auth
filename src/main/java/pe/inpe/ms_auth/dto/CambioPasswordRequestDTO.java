package pe.inpe.ms_auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CambioPasswordRequestDTO {

    @NotBlank(message = "El password actual es obligatorio")
    private String passwordActual;

    @NotBlank(message = "El nuevo password es obligatorio")
    @Size(min = 8, message = "El password debe tener al menos 8 caracteres")
    private String passwordNuevo;
}