package com.biblioteca.service.impl;

import com.biblioteca.exception.BusinessException;
import com.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.model.dto.EmprestimoDTO;
import com.biblioteca.model.entity.Emprestimo;
import com.biblioteca.model.entity.Livro;
import com.biblioteca.model.entity.Usuario;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.LivroRepository;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.service.EmprestimoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Empréstimo.
 * 
 * Implementa regras de negócio para empréstimos de livros.
 */
@Service
@Transactional
public class EmprestimoServiceImpl implements EmprestimoService {

    private static final int MAX_EMPRESTIMOS_ATIVOS = 3;

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;

    public EmprestimoServiceImpl(EmprestimoRepository emprestimoRepository,
                                 UsuarioRepository usuarioRepository,
                                 LivroRepository livroRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmprestimoDTO> listarTodos() {
        return emprestimoRepository.findAll().stream()
                .map(EmprestimoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmprestimoDTO> buscarPorId(Long id) {
        Emprestimo emprestimo = emprestimoRepository.findByIdWithRelations(id);
        return Optional.ofNullable(emprestimo).map(EmprestimoDTO::fromEntity);
    }

    @Override
    public EmprestimoDTO realizarEmprestimo(Long usuarioId, Long livroId) {
        // Buscar usuário
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", usuarioId));

        // Verificar se usuário está ativo
        if (!usuario.isAtivo()) {
            throw new BusinessException("Usuário inativo não pode realizar empréstimos");
        }

        // Buscar livro
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", livroId));

        // Verificar disponibilidade
        if (!livro.isDisponivel()) {
            throw new BusinessException("Livro não está disponível para empréstimo");
        }

        // Verificar se usuário já tem empréstimo ativo deste livro
        if (usuarioPossuiEmprestimoAtivo(usuarioId, livroId)) {
            throw new BusinessException("Usuário já possui um empréstimo ativo deste livro");
        }

        // Verificar limite de empréstimos
        List<Emprestimo> emprestimosAtivos = emprestimoRepository.findEmprestimosAtivosPorUsuario(usuarioId);
        if (emprestimosAtivos.size() >= MAX_EMPRESTIMOS_ATIVOS) {
            throw new BusinessException("Usuário atingiu o limite de " + MAX_EMPRESTIMOS_ATIVOS + " empréstimos ativos");
        }

        // Criar empréstimo
        Emprestimo emprestimo = Emprestimo.builder()
                .usuario(usuario)
                .livro(livro)
                .build();

        // Atualizar disponibilidade do livro
        livro.emprestar();
        livroRepository.save(livro);

        Emprestimo salvo = emprestimoRepository.save(emprestimo);
        return EmprestimoDTO.fromEntity(salvo);
    }

    @Override
    public EmprestimoDTO realizarDevolucao(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findByIdWithRelations(emprestimoId);
        if (emprestimo == null) {
            throw new ResourceNotFoundException("Empréstimo", emprestimoId);
        }

        if (emprestimo.getStatus() == Emprestimo.StatusEmprestimo.DEVOLVIDO) {
            throw new BusinessException("Este empréstimo já foi devolvido");
        }

        emprestimo.realizarDevolucao();
        Emprestimo salvo = emprestimoRepository.save(emprestimo);
        return EmprestimoDTO.fromEntity(salvo);
    }

    @Override
    public EmprestimoDTO renovarEmprestimo(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findByIdWithRelations(emprestimoId);
        if (emprestimo == null) {
            throw new ResourceNotFoundException("Empréstimo", emprestimoId);
        }

        if (emprestimo.getStatus() == Emprestimo.StatusEmprestimo.DEVOLVIDO) {
            throw new BusinessException("Não é possível renovar empréstimo já devolvido");
        }

        if (emprestimo.isAtrasado()) {
            throw new BusinessException("Não é possível renovar empréstimo em atraso. Por favor, devolva o livro primeiro.");
        }

        if (emprestimo.getStatus() == Emprestimo.StatusEmprestimo.RENOVADO) {
            throw new BusinessException("Empréstimo já foi renovado uma vez. Não é permitida nova renovação.");
        }

        emprestimo.renovar();
        Emprestimo salvo = emprestimoRepository.save(emprestimo);
        return EmprestimoDTO.fromEntity(salvo);
    }

    @Override
    public void cancelarEmprestimo(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findByIdWithRelations(emprestimoId);
        if (emprestimo == null) {
            throw new ResourceNotFoundException("Empréstimo", emprestimoId);
        }

        if (emprestimo.getStatus() == Emprestimo.StatusEmprestimo.DEVOLVIDO) {
            throw new BusinessException("Não é possível cancelar empréstimo já devolvido");
        }

        emprestimo.setStatus(Emprestimo.StatusEmprestimo.CANCELADO);
        emprestimo.getLivro().devolver();
        
        livroRepository.save(emprestimo.getLivro());
        emprestimoRepository.save(emprestimo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmprestimoDTO> listarPorUsuario(Long usuarioId) {
        return emprestimoRepository.findByUsuarioId(usuarioId).stream()
                .map(EmprestimoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmprestimoDTO> listarAtivos() {
        return emprestimoRepository.findEmprestimosAtivos().stream()
                .map(EmprestimoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmprestimoDTO> listarAtrasados() {
        return emprestimoRepository.findEmprestimosAtrasados(LocalDate.now()).stream()
                .map(EmprestimoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmprestimoDTO> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return emprestimoRepository.findByPeriodo(inicio, fim).stream()
                .map(EmprestimoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarAtivos() {
        return emprestimoRepository.countEmprestimosAtivos();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarAtrasados() {
        return emprestimoRepository.countEmprestimosAtrasados(LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean usuarioPossuiEmprestimoAtivo(Long usuarioId, Long livroId) {
        return !emprestimoRepository.findEmprestimoAtivo(usuarioId, livroId).isEmpty();
    }
}
