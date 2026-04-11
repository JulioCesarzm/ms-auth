package pe.inpe.ms_auth.service;


import pe.inpe.ms_auth.dto.CambioPasswordRequestDTO;
import pe.inpe.ms_auth.dto.UsuarioRequestDTO;
import pe.inpe.ms_auth.dto.UsuarioResponseDTO;
import pe.inpe.ms_auth.dto.UsuarioUpdateRequestDTO;
import pe.inpe.ms_auth.entity.Usuario;

import java.util.List;

public interface IUsuarioService {

    Usuario getUsuarioByUsername(String username);

    List<String> getRolesByUsername(String username);

    UsuarioResponseDTO obtenerPorId(Long id);

    List<UsuarioResponseDTO> listarTodos();

    UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request);

    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioUpdateRequestDTO request);

    void cambiarEstado(Long id, Boolean estado);

    void cambiarPassword(Long id, CambioPasswordRequestDTO request);

    void actualizarUltimoAcceso(String username);
}