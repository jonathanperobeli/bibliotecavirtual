package com.biblioteca.service;

import com.biblioteca.model.dto.EmprestimoDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço para operações com Empréstimo.
 */
public interface EmprestimoService {

    List<EmprestimoDTO> listarTodos();

    Optional<EmprestimoDTO> buscarPorId(Long id);

    EmprestimoDTO realizarEmprestimo(Long usuarioId, Long livroId);

    EmprestimoDTO realizarDevolucao(Long emprestimoId);

    EmprestimoDTO renovarEmprestimo(Long emprestimoId);

    void cancelarEmprestimo(Long emprestimoId);

    List<EmprestimoDTO> listarPorUsuario(Long usuarioId);

    List<EmprestimoDTO> listarAtivos();

    List<EmprestimoDTO> listarAtrasados();

    List<EmprestimoDTO> listarPorPeriodo(LocalDate inicio, LocalDate fim);

    Long contarAtivos();

    Long contarAtrasados();

    boolean usuarioPossuiEmprestimoAtivo(Long usuarioId, Long livroId);
}
