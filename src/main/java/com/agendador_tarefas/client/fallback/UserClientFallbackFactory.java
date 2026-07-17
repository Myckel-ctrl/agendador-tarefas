package com.agendador_tarefas.client.fallback;

import com.agendador_tarefas.client.UserClient;
import com.agendador_tarefas.exception.ResourceNotFoundException;
import com.agendador_tarefas.exception.ServiceUnavailableException;
import com.agendador_tarefas.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

    private static final Logger log = LoggerFactory.getLogger(UserClientFallbackFactory.class);

    @Override
    public UserClient create(Throwable cause) {
        return authorizationHeader -> {
            log.warn("Falha ao comunicar com o microsserviço de usuários: {}", cause.getMessage());

            if (cause instanceof UnauthorizedException unauthorized) {
                throw unauthorized;
            }
            if (cause instanceof ResourceNotFoundException resourceNotFound) {
                throw resourceNotFound;
            }
            throw new ServiceUnavailableException(
                    "Não foi possível validar a autenticação no momento. Tente novamente em instantes.");
        };
    }
}
