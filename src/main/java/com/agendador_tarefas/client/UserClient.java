package com.agendador_tarefas.client;

import com.agendador_tarefas.client.config.FeignClientConfiguration;
import com.agendador_tarefas.client.dto.UserResponseDTO;
import com.agendador_tarefas.client.fallback.UserClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "usuario-service",
        url = "${services.usuario.url}",
        configuration = FeignClientConfiguration.class,
        fallbackFactory = UserClientFallbackFactory.class
)
public interface UserClient {

    @GetMapping("/users/me")
    UserResponseDTO buscarUsuarioAutenticado(@RequestHeader("Authorization") String authorizationHeader);
}
