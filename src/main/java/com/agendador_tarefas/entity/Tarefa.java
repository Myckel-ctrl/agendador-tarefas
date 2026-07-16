package com.agendador_tarefas.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "tarefas")
@CompoundIndexes({
        @CompoundIndex(name = "usuario_vencimento_idx", def = "{'usuarioId': 1, 'dataVencimento': 1}")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarefa {

    @Id
    private String id;

    private String titulo;

    private String descricao;

    private StatusTarefa status;

    private LocalDateTime dataVencimento;

    @Indexed
    private Long usuarioId;

    @CreatedDate
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    private LocalDateTime dataAlteracao;
}
