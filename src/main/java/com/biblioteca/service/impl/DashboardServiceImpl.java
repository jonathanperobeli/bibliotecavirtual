package com.biblioteca.service.impl;

import com.biblioteca.model.dto.DashboardDTO;
import com.biblioteca.repository.*;
import com.biblioteca.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Implementação do serviço de Dashboard.
 * 
 * Agrega estatísticas de todas as entidades do sistema.
 */
@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final EmprestimoRepository emprestimoRepository;

    public DashboardServiceImpl(LivroRepository livroRepository,
                                UsuarioRepository usuarioRepository,
                                AutorRepository autorRepository,
                                CategoriaRepository categoriaRepository,
                                EmprestimoRepository emprestimoRepository) {
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    @Override
    public DashboardDTO obterEstatisticas() {
        return DashboardDTO.builder()
                .totalLivros(livroRepository.count())
                .totalUsuarios(usuarioRepository.countUsuariosAtivos())
                .totalAutores(autorRepository.count())
                .totalCategorias(categoriaRepository.count())
                .emprestimosAtivos(emprestimoRepository.countEmprestimosAtivos())
                .emprestimosAtrasados(emprestimoRepository.countEmprestimosAtrasados(LocalDate.now()))
                .livrosDisponiveis(livroRepository.countLivrosDisponiveis())
                .livrosIndisponiveis(livroRepository.countLivrosIndisponiveis())
                .build();
    }
}
