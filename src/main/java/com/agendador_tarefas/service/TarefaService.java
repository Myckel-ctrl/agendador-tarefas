package com.agendador_tarefas.service;

import com.agendador_tarefas.dto.StatusUpdateDTO;
import com.agendador_tarefas.dto.TarefaRequestDTO;
import com.agendador_tarefas.dto.TarefaResponseDTO;
import com.agendador_tarefas.dto.TarefaUpdateDTO;
import com.agendador_tarefas.security.AuthenticatedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TarefaService {

    TarefaResponseDTO criar(AuthenticatedUser usuarioLogado, TarefaRequestDTO dto);

    TarefaResponseDTO buscarPorId(AuthenticatedUser usuarioLogado, String id);

    List<TarefaResponseDTO> listarMinhasTarefas(AuthenticatedUser usuarioLogado);

    List<TarefaResponseDTO> buscarPorPeriodo(AuthenticatedUser usuarioLogado,
                                             LocalDateTime inicio,
                                             LocalDateTime fim);

    TarefaResponseDTO atualizar(AuthenticatedUser usuarioLogado, String id, TarefaUpdateDTO dto);

    TarefaResponseDTO atualizarStatus(AuthenticatedUser usuarioLogado, String id, StatusUpdateDTO dto);

    void deletar(AuthenticatedUser usuarioLogado, String id);
}
