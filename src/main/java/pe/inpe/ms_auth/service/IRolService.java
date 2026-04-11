package pe.inpe.ms_auth.service;

import pe.inpe.ms_auth.dto.RolResponseDTO;

import java.util.List;

public interface IRolService {

    List<RolResponseDTO> listarTodos();

    RolResponseDTO obtenerPorId(Long id);
}
