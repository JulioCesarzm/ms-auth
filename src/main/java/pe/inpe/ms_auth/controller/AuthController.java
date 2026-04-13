package pe.inpe.ms_auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import pe.inpe.ms_auth.dto.*;
import pe.inpe.ms_auth.entity.Usuario;
import pe.inpe.ms_auth.service.IJwtService;
import pe.inpe.ms_auth.service.IRolService;
import pe.inpe.ms_auth.service.IUsuarioService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUsuarioService usuarioService;
    private final IRolService rolService;
    private final IJwtService jwtService;
    private final AuthenticationManager authManager;

    @PostMapping("/login")
    public ResponseEntity<GenericResponseDTO<UsuarioJwtDTO>> login(@Valid @RequestBody LoginRequestDTO login) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword())
            );

            Usuario usuario = usuarioService.getUsuarioByUsername(login.getUsername());
            List<GrantedAuthority> authorities = List.copyOf(authentication.getAuthorities());

            String token = jwtService.generationToken(usuario, authorities);
            usuarioService.actualizarUltimoAcceso(login.getUsername());

            UsuarioJwtDTO usuarioJwt = UsuarioJwtDTO.builder()
                    .idUsuario(usuario.getIdUsuario())
                    .username(usuario.getUsername())
                    .rol(usuario.getRol().getNombre())
                    .token(token)
                    .expiresIn(jwtService.getExpirationTime())
                    .build();

            log.info("Login exitoso para usuario: {}", login.getUsername());

            return ResponseEntity.ok(GenericResponseDTO.<UsuarioJwtDTO>builder().response(usuarioJwt).build());

        } catch (BadCredentialsException e) {
            log.warn("Intento de login fallido para usuario: {}", login.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(GenericResponseDTO.<UsuarioJwtDTO>builder()
                            .error(ErrorMessageDTO.builder()
                                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                                    .message("Usuario y/o password incorrectos")
                                    .dateError(LocalDateTime.now())
                                    .build())
                            .build());
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<GenericResponseDTO<Boolean>> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(GenericResponseDTO.<Boolean>builder().response(false).build());
        }
        String token = authHeader.substring(7);
        boolean isValid = jwtService.tokenValid(token);
        return ResponseEntity.ok(GenericResponseDTO.<Boolean>builder().response(isValid).build());
    }

    @GetMapping("/me")
    public ResponseEntity<GenericResponseDTO<MeResponseDTO>> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        var claims = jwtService.getClaims(token);
        MeResponseDTO usuarioJwt = MeResponseDTO.builder()
                .idUsuario(claims.get("userId", Long.class))
                .username(claims.getSubject())
                .build();
        return ResponseEntity.ok(GenericResponseDTO.<MeResponseDTO>builder().response(usuarioJwt).build());
    }

    @GetMapping("/roles")
    public ResponseEntity<GenericResponseDTO<List<?>>> listarRoles() {
        return ResponseEntity.ok(GenericResponseDTO.<List<?>>builder().response(rolService.listarTodos()).build());
    }

    @PostMapping("/usuarios")
    public ResponseEntity<GenericResponseDTO<UsuarioResponseDTO>> crearUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO response = usuarioService.crearUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GenericResponseDTO.<UsuarioResponseDTO>builder().response(response).build());
    }

    @GetMapping("/usuarios")
    public ResponseEntity<GenericResponseDTO<List<UsuarioResponseDTO>>> listarUsuarios() {
        List<UsuarioResponseDTO> response = usuarioService.listarTodos();
        return ResponseEntity.ok(GenericResponseDTO.<List<UsuarioResponseDTO>>builder().response(response).build());
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<GenericResponseDTO<UsuarioResponseDTO>> obtenerUsuario(@PathVariable Long id) {
        UsuarioResponseDTO response = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(GenericResponseDTO.<UsuarioResponseDTO>builder().response(response).build());
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<GenericResponseDTO<UsuarioResponseDTO>> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateRequestDTO request) {
        UsuarioResponseDTO response = usuarioService.actualizarUsuario(id, request);
        return ResponseEntity.ok(GenericResponseDTO.<UsuarioResponseDTO>builder().response(response).build());
    }

    @PatchMapping("/usuarios/{id}/estado")
    public ResponseEntity<GenericResponseDTO<Void>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Boolean estado) {
        usuarioService.cambiarEstado(id, estado);
        return ResponseEntity.ok(GenericResponseDTO.<Void>builder().build());
    }

    @PatchMapping("/usuarios/{id}/password")
    public ResponseEntity<GenericResponseDTO<Void>> cambiarPassword(
            @PathVariable Long id,
            @Valid @RequestBody CambioPasswordRequestDTO request) {
        usuarioService.cambiarPassword(id, request);
        return ResponseEntity.ok(GenericResponseDTO.<Void>builder().build());
    }
}
