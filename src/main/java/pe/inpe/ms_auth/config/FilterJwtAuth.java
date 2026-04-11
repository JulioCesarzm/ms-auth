package pe.inpe.ms_auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.inpe.ms_auth.service.IJwtService;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilterJwtAuth extends OncePerRequestFilter {

    private final IJwtService jwtService;

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/validate",
            "/actuator"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String path = request.getRequestURI();

            if (isPublicPath(path)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = jwtService.extractToken(request);
            if (token != null && jwtService.tokenValid(token)) {
                Claims claims = jwtService.getClaims(token);
                jwtService.generationAuthentication(claims);
            } else {
                SecurityContextHolder.clearContext();
            }

            filterChain.doFilter(request, response);

        } catch (JwtException ex) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token inválido o expirado\"}");
        }
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }
}