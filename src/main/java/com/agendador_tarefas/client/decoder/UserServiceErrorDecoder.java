package com.agendador_tarefas.client.decoder;

import com.agendador_tarefas.exception.ResourceNotFoundException;
import com.agendador_tarefas.exception.ServiceUnavailableException;
import com.agendador_tarefas.exception.UnauthorizedException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class UserServiceErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 401, 403 -> new UnauthorizedException("Token inválido, expirado ou usuário sem permissão.");
            case 404 -> new ResourceNotFoundException("Usuário autenticado não encontrado no microsserviço de usuários.");
            case 500, 502, 503, 504 -> new ServiceUnavailableException("Microsserviço de usuários indisponível no momento.");
            default -> defaultDecoder.decode(methodKey, response);
        };
    }
}
