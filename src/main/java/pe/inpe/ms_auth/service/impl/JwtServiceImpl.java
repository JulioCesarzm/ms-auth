package pe.inpe.ms_auth.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pe.inpe.ms_auth.entity.Usuario;
import pe.inpe.ms_auth.service.IJwtService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class JwtServiceImpl implements IJwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSignKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generationToken(Usuario user, List<GrantedAuthority> authorities) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .id(user.getIdUsuario().toString())
                .subject(user.getUsername())
                .claim("authorities", authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .claim("userId", user.getIdUsuario())
                .claim("email", user.getEmail())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignKey())
                .compact();
    }

    @Override
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public boolean tokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    @Override
    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    @Override
    public void generationAuthentication(Claims claims) {
        List<String> authoritiesList = claims.get("authorities", List.class);

        List<SimpleGrantedAuthority> authorities = authoritiesList.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                authorities
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    public Long getExpirationTime() {
        return expiration / 1000;
    }
}