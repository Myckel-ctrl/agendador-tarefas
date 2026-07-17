package com.agendador_tarefas.dto;

import com.agendador_tarefas.entity.StatusTarefa;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarefaResponseDTO {
    private String id;
    private String titulo;
    private String descricao;
    private StatusTarefa status;
    private LocalDateTime dataVencimento;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;
    private Long usuarioId;
}
