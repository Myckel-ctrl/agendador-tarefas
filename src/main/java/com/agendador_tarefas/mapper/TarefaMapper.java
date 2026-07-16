package com.agendador_tarefas.mapper;

import com.agendador_tarefas.dto.TarefaRequestDTO;
import com.agendador_tarefas.dto.TarefaResponseDTO;
import com.agendador_tarefas.dto.TarefaUpdateDTO;
import com.agendador_tarefas.entity.Tarefa;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TarefaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "usuarioId", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAlteracao", ignore = true)
    Tarefa toEntity(TarefaRequestDTO dto);

    TarefaResponseDTO toResponseDTO(Tarefa entity);

    List<TarefaResponseDTO> toResponseDTOList(List<Tarefa> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "usuarioId", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAlteracao", ignore = true)
    void updateEntityFromDto(TarefaUpdateDTO dto, @MappingTarget Tarefa entity);
}
