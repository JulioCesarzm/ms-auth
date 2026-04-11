package pe.inpe.ms_auth.mapper;

import org.mapstruct.Mapper;
import pe.inpe.ms_auth.dto.RolResponseDTO;
import pe.inpe.ms_auth.entity.RolSistema;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IRolMapper {

    RolResponseDTO toResponse(RolSistema rol);

    List<RolResponseDTO> toResponseList(List<RolSistema> roles);
}
