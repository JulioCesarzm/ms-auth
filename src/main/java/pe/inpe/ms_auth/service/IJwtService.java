package pe.inpe.ms_auth.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import pe.inpe.ms_auth.entity.Usuario;

import java.util.List;

public interface IJwtService {

    String generationToken(Usuario user, List<GrantedAuthority> authorities);

    Claims getClaims(String token);

    boolean tokenValid(String token);

    String extractToken(HttpServletRequest request);

    void generationAuthentication(Claims claims);

    Long getExpirationTime();
}