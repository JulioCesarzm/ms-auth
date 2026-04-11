package pe.inpe.ms_auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.inpe.ms_auth.dto.UsuarioResponseDTO;
import pe.inpe.ms_auth.entity.Usuario;

@Mapper(componentModel = "spring")
public interface IUsuarioMapper {

    @Mapping(target = "rol", source = "rol.nombre")
    UsuarioResponseDTO toResponse(Usuario usuario);
}
