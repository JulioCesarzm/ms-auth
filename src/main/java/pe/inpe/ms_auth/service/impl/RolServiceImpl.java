package pe.inpe.ms_auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.inpe.ms_auth.dto.RolResponseDTO;
import pe.inpe.ms_auth.exception.ResourceNotFoundException;
import pe.inpe.ms_auth.mapper.IRolMapper;
import pe.inpe.ms_auth.repository.IRolSistemaRepository;
import pe.inpe.ms_auth.service.IRolService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements IRolService {

    private final IRolSistemaRepository rolRepository;
    private final IRolMapper rolMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RolResponseDTO> listarTodos() {
        return rolMapper.toResponseList(rolRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public RolResponseDTO obtenerPorId(Long id) {
        return rolRepository.findById(id)
                .map(rolMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + id));
    }
}
