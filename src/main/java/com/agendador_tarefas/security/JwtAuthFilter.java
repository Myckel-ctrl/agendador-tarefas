package com.agendador_tarefas.security;

import com.agendador_tarefas.client.UserClient;
import com.agendador_tarefas.client.dto.UserResponseDTO;
import com.agendador_tarefas.exception.ServiceUnavailableException;
import com.agendador_tarefas.exception.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTH_ERROR_ATTRIBUTE = "authError";

    private final JwtUtil jwtUtil;
    private final UserClient userClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        if (!jwtUtil.isTokenValid(token)) {
            log.debug("Token JWT inválido ou expirado.");
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                autenticarViaUsuarioService(request, authHeader);
            } catch (UnauthorizedException | ServiceUnavailableException ex) {
                log.warn("Falha ao autenticar via usuario-service: {}", ex.getMessage());
                request.setAttribute(AUTH_ERROR_ATTRIBUTE, ex);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void autenticarViaUsuarioService(HttpServletRequest request, String authHeader) {
        UserResponseDTO usuario = userClient.buscarUsuarioAutenticado(authHeader);

        AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
                .id(usuario.getId())
                .name(usuario.getName())
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .build();

        var authToken = new UsernamePasswordAuthenticationToken(
                authenticatedUser, null, authenticatedUser.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
