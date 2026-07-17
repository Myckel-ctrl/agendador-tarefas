package com.agendador_tarefas.service;

import com.agendador_tarefas.dto.StatusUpdateDTO;
import com.agendador_tarefas.dto.TarefaRequestDTO;
import com.agendador_tarefas.dto.TarefaResponseDTO;
import com.agendador_tarefas.dto.TarefaUpdateDTO;
import com.agendador_tarefas.entity.StatusTarefa;
import com.agendador_tarefas.entity.Tarefa;
import com.agendador_tarefas.exception.AccessDeniedException;
import com.agendador_tarefas.exception.InvalidDateRangeException;
import com.agendador_tarefas.exception.ResourceNotFoundException;
import com.agendador_tarefas.mapper.TarefaMapper;
import com.agendador_tarefas.repository.TarefaRepository;
import com.agendador_tarefas.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaServiceImpl implements TarefaService {

    private final TarefaRepository tarefaRepository;
    private final TarefaMapper tarefaMapper;

    @Override
    @Transactional
    public TarefaResponseDTO criar(AuthenticatedUser usuarioLogado, TarefaRequestDTO dto) {
        Tarefa tarefa = tarefaMapper.toEntity(dto);
        tarefa.setStatus(StatusTarefa.PENDENTE);
        tarefa.setUsuarioId(usuarioLogado.getId());

        return tarefaMapper.toResponseDTO(tarefaRepository.save(tarefa));
    }

    @Override
    public TarefaResponseDTO buscarPorId(AuthenticatedUser usuarioLogado, String id) {
        Tarefa tarefa = buscarTarefaOuFalhar(id);
        validarPropriedade(tarefa, usuarioLogado);
        return tarefaMapper.toResponseDTO(tarefa);
    }

    @Override
    public List<TarefaResponseDTO> listarMinhasTarefas(AuthenticatedUser usuarioLogado) {
        return tarefaMapper.toResponseDTOList(
                tarefaRepository.findByUsuarioId(usuarioLogado.getId())
        );
    }

    @Override
    public List<TarefaResponseDTO> buscarPorPeriodo(AuthenticatedUser usuarioLogado,
                                                    LocalDateTime inicio,
                                                    LocalDateTime fim) {
        if (inicio.isAfter(fim)) {
            throw new InvalidDateRangeException("A data inicial não pode ser posterior à data final.");
        }

        return tarefaMapper.toResponseDTOList(
                tarefaRepository.findByUsuarioIdAndDataVencimentoBetween(
                        usuarioLogado.getId(), inicio, fim)
        );
    }

    @Override
    @Transactional
    public TarefaResponseDTO atualizar(AuthenticatedUser usuarioLogado, String id, TarefaUpdateDTO dto) {
        Tarefa tarefa = buscarTarefaOuFalhar(id);
        validarPropriedade(tarefa, usuarioLogado);

        tarefaMapper.updateEntityFromDto(dto, tarefa);

        return tarefaMapper.toResponseDTO(tarefaRepository.save(tarefa));
    }

    @Override
    @Transactional
    public TarefaResponseDTO atualizarStatus(AuthenticatedUser usuarioLogado, String id, StatusUpdateDTO dto) {
        Tarefa tarefa = buscarTarefaOuFalhar(id);
        validarPropriedade(tarefa, usuarioLogado);

        tarefa.setStatus(dto.getStatus());

        return tarefaMapper.toResponseDTO(tarefaRepository.save(tarefa));
    }

    @Override
    @Transactional
    public void deletar(AuthenticatedUser usuarioLogado, String id) {
        Tarefa tarefa = buscarTarefaOuFalhar(id);
        validarPropriedade(tarefa, usuarioLogado);
        tarefaRepository.delete(tarefa);
    }

    private Tarefa buscarTarefaOuFalhar(String id) {
        if (!ObjectId.isValid(id)) {
            throw new ResourceNotFoundException("Tarefa", id);
        }
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa", id));
    }

    private void validarPropriedade(Tarefa tarefa, AuthenticatedUser usuarioLogado) {
        if (!tarefa.getUsuarioId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você só pode acessar ou modificar suas próprias tarefas.");
        }
    }
}
