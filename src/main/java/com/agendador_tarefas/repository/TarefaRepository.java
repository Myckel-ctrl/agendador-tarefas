package com.agendador_tarefas.repository;

import com.agendador_tarefas.entity.Tarefa;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TarefaRepository extends MongoRepository<Tarefa, String> {

    List<Tarefa> findByUsuarioId(Long usuarioId);

    List<Tarefa> findByUsuarioIdAndDataVencimentoBetween(Long usuarioId, LocalDateTime inicio, LocalDateTime fim);
}
