package com.agendador_tarefas.controller;

import com.agendador_tarefas.dto.StatusUpdateDTO;
import com.agendador_tarefas.dto.TarefaRequestDTO;
import com.agendador_tarefas.dto.TarefaResponseDTO;
import com.agendador_tarefas.dto.TarefaUpdateDTO;
import com.agendador_tarefas.security.AuthenticatedUser;
import com.agendador_tarefas.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
@Tag(name = "Tarefas", description = "Gerenciamento de tarefas do usuário autenticado")
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    @Operation(summary = "Cria uma nova tarefa para o usuário autenticado (status inicial: PENDENTE)")
    public ResponseEntity<TarefaResponseDTO> criar(@AuthenticationPrincipal AuthenticatedUser usuarioLogado,
                                                   @Valid @RequestBody TarefaRequestDTO dto) {
        TarefaResponseDTO tarefa = tarefaService.criar(usuarioLogado, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefa);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma tarefa pelo ID (apenas se pertencer ao usuário autenticado)")
    public ResponseEntity<TarefaResponseDTO> buscarPorId(
            @AuthenticationPrincipal AuthenticatedUser usuarioLogado,
            @PathVariable String id) {
        return ResponseEntity.ok(tarefaService.buscarPorId(usuarioLogado, id));
    }

    @GetMapping
    public ResponseEntity<List<TarefaResponseDTO>> listarMinhasTarefas(
            @AuthenticationPrincipal AuthenticatedUser usuarioLogado) {
        return ResponseEntity.ok(tarefaService.listarMinhasTarefas(usuarioLogado));
    }

    @GetMapping("/periodo")
    @Operation(summary = "Busca as tarefas do usuário autenticado dentro de um período (dataVencimento)")
    public ResponseEntity<List<TarefaResponseDTO>> buscarPorPeriodo(
            @AuthenticationPrincipal AuthenticatedUser usuarioLogado,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(tarefaService.buscarPorPeriodo(usuarioLogado, inicio, fim));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza parcialmente uma tarefa (campos não enviados permanecem inalterados)")
    public ResponseEntity<TarefaResponseDTO> atualizar(
            @AuthenticationPrincipal AuthenticatedUser usuarioLogado,
            @PathVariable String id,
            @Valid @RequestBody TarefaUpdateDTO dto) {
        return ResponseEntity.ok(tarefaService.atualizar(usuarioLogado, id, dto));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Altera apenas o status de uma tarefa")
    public ResponseEntity<TarefaResponseDTO> atualizarStatus(
            @AuthenticationPrincipal AuthenticatedUser usuarioLogado,
            @PathVariable String id,
            @Valid @RequestBody StatusUpdateDTO dto) {
        return ResponseEntity.ok(tarefaService.atualizarStatus(usuarioLogado, id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma tarefa do usuário autenticado")
    public ResponseEntity<Void> deletar(
            @AuthenticationPrincipal AuthenticatedUser usuarioLogado,
            @PathVariable String id) {
        tarefaService.deletar(usuarioLogado, id);
        return ResponseEntity.noContent().build();
    }
}
