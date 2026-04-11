package pe.inpe.ms_auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.inpe.ms_auth.dto.CambioPasswordRequestDTO;
import pe.inpe.ms_auth.dto.UsuarioRequestDTO;
import pe.inpe.ms_auth.dto.UsuarioResponseDTO;
import pe.inpe.ms_auth.dto.UsuarioUpdateRequestDTO;
import pe.inpe.ms_auth.entity.RolSistema;
import pe.inpe.ms_auth.entity.Usuario;
import pe.inpe.ms_auth.exception.BadRequestException;
import pe.inpe.ms_auth.exception.ResourceNotFoundException;
import pe.inpe.ms_auth.mapper.IUsuarioMapper;
import pe.inpe.ms_auth.repository.IRolSistemaRepository;
import pe.inpe.ms_auth.repository.IUsuarioRepository;
import pe.inpe.ms_auth.service.IUsuarioService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final IRolSistemaRepository rolRepository;
    private final IUsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Usuario getUsuarioByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getRolesByUsername(String username) {
        return usuarioRepository.findRolesByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request) {
        log.info("Creando nuevo usuario: {}", request.getUsername());

        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("El username ya está en uso: " + request.getUsername());
        }
        if (request.getEmail() != null && usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está en uso: " + request.getEmail());
        }

        RolSistema rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + request.getIdRol()));

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usuario.setEmail(request.getEmail());
        usuario.setRol(rol);
        usuario.setIdPersona(request.getIdPersona());
        usuario.setEstado(true);

        usuario = usuarioRepository.save(usuario);
        log.info("Usuario creado con ID: {}", usuario.getIdUsuario());

        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioUpdateRequestDTO request) {
        log.info("Actualizando usuario ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        if (request.getEmail() != null) {
            usuario.setEmail(request.getEmail());
        }

        if (request.getIdRol() != null) {
            RolSistema rol = rolRepository.findById(request.getIdRol())
                    .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
            usuario.setRol(rol);
        }

        if (request.getIdPersona() != null) {
            usuario.setIdPersona(request.getIdPersona());
        }

        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional
    public void cambiarEstado(Long id, Boolean estado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        usuario.setEstado(estado);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void cambiarPassword(Long id, CambioPasswordRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPasswordActual(), usuario.getPasswordHash())) {
            throw new BadRequestException("El password actual es incorrecto");
        }

        usuario.setPasswordHash(passwordEncoder.encode(request.getPasswordNuevo()));
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void actualizarUltimoAcceso(String username) {
        Usuario usuario = getUsuarioByUsername(username);
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }
}
