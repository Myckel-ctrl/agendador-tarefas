package com.agendador_tarefas.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.agendador_tarefas.client")
public class FeignClientsConfig {
}
