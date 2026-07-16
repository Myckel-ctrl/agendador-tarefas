package com.agendador_tarefas.dto;

import com.agendador_tarefas.entity.StatusTarefa;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateDTO {

    @NotNull(message = "O status é obrigatório.")
    private StatusTarefa status;
}
