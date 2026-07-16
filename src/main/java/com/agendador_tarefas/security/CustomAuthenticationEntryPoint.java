package com.agendador_tarefas.security;

import com.agendador_tarefas.exception.ApiError;
import com.agendador_tarefas.exception.ServiceUnavailableException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        Object authError = request.getAttribute("authError");

        HttpStatus status = authError instanceof ServiceUnavailableException
                ? HttpStatus.SERVICE_UNAVAILABLE
                : HttpStatus.UNAUTHORIZED;

        String message = authError instanceof Exception exception
                ? exception.getMessage()
                : "Autenticação necessária. Token ausente, inválido ou expirado.";

        ApiError apiError = new ApiError(status.value(), status.getReasonPhrase(), message);

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(apiError));
    }
}
