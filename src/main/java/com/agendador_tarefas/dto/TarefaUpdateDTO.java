package com.agendador_tarefas.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TarefaUpdateDTO {

    @Size(max = 120, message = "O título deve ter no máximo 120 caracteres.")
    private String titulo;

    @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres.")
    private String descricao;

    @Future(message = "A data de vencimento deve estar no futuro.")
    private LocalDateTime dataVencimento;
}
