package com.biblioteca.service.impl;

import com.biblioteca.exception.BusinessException;
import com.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.model.dto.LivroDTO;
import com.biblioteca.model.entity.Autor;
import com.biblioteca.model.entity.Categoria;
import com.biblioteca.model.entity.Livro;
import com.biblioteca.repository.AutorRepository;
import com.biblioteca.repository.CategoriaRepository;
import com.biblioteca.repository.LivroRepository;
import com.biblioteca.service.LivroService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Livro.
 */
@Service
@Transactional
public class LivroServiceImpl implements LivroService {

    private final LivroRepository livroRepository;
    private final CategoriaRepository categoriaRepository;
    private final AutorRepository autorRepository;

    public LivroServiceImpl(LivroRepository livroRepository, 
                           CategoriaRepository categoriaRepository,
                           AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.categoriaRepository = categoriaRepository;
        this.autorRepository = autorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivroDTO> listarTodos() {
        return livroRepository.findAll().stream()
                .map(LivroDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LivroDTO> listarPaginado(Pageable pageable) {
        return livroRepository.findAll(pageable)
                .map(LivroDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LivroDTO> buscarPorId(Long id) {
        return livroRepository.findByIdWithRelations(id)
                .map(LivroDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Livro> buscarEntidadePorId(Long id) {
        return livroRepository.findById(id);
    }

    @Override
    public LivroDTO salvar(LivroDTO livroDTO) {
        // Verificar ISBN duplicado
        if (livroDTO.getIsbn() != null && livroRepository.existsByIsbn(livroDTO.getIsbn())) {
            throw new BusinessException("ISBN já cadastrado: " + livroDTO.getIsbn());
        }

        Livro livro = livroDTO.toEntity();

        // Associar categoria
        if (livroDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(livroDTO.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria", livroDTO.getCategoriaId()));
            livro.setCategoria(categoria);
        }

        // Associar autores
        if (livroDTO.getAutoresIds() != null && !livroDTO.getAutoresIds().isEmpty()) {
            Set<Autor> autores = new HashSet<>();
            for (Long autorId : livroDTO.getAutoresIds()) {
                Autor autor = autorRepository.findById(autorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Autor", autorId));
                autores.add(autor);
            }
            livro.setAutores(autores);
        }

        Livro salvo = livroRepository.save(livro);
        return LivroDTO.fromEntity(salvo);
    }

    @Override
    public LivroDTO atualizar(Long id, LivroDTO livroDTO) {
        Livro existente = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", id));

        // Verificar ISBN duplicado (se diferente do atual)
        if (livroDTO.getIsbn() != null 
                && !livroDTO.getIsbn().equals(existente.getIsbn())
                && livroRepository.existsByIsbn(livroDTO.getIsbn())) {
            throw new BusinessException("ISBN já cadastrado: " + livroDTO.getIsbn());
        }

        existente.setTitulo(livroDTO.getTitulo());
        existente.setIsbn(livroDTO.getIsbn());
        existente.setAnoPublicacao(livroDTO.getAnoPublicacao());
        existente.setEditora(livroDTO.getEditora());
        existente.setEdicao(livroDTO.getEdicao());
        existente.setNumeroPaginas(livroDTO.getNumeroPaginas());
        existente.setSinopse(livroDTO.getSinopse());
        existente.setQuantidadeTotal(livroDTO.getQuantidadeTotal());
        existente.setUrlCapa(livroDTO.getUrlCapa());

        if (livroDTO.getStatus() != null) {
            existente.setStatus(livroDTO.getStatus());
        }

        // Atualizar categoria
        if (livroDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(livroDTO.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria", livroDTO.getCategoriaId()));
            existente.setCategoria(categoria);
        }

        // Atualizar autores
        if (livroDTO.getAutoresIds() != null) {
            Set<Autor> autores = new HashSet<>();
            for (Long autorId : livroDTO.getAutoresIds()) {
                Autor autor = autorRepository.findById(autorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Autor", autorId));
                autores.add(autor);
            }
            existente.setAutores(autores);
        }

        Livro atualizado = livroRepository.save(existente);
        return LivroDTO.fromEntity(atualizado);
    }

    @Override
    public void deletar(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", id));

        if (!livro.getEmprestimos().isEmpty()) {
            throw new BusinessException("Não é possível excluir livro com empréstimos registrados");
        }

        livroRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivroDTO> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo).stream()
                .map(LivroDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivroDTO> buscarPorCategoria(Long categoriaId) {
        return livroRepository.findByCategoriaId(categoriaId).stream()
                .map(LivroDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivroDTO> buscarPorAutor(Long autorId) {
        return livroRepository.findByAutorId(autorId).stream()
                .map(LivroDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivroDTO> listarDisponiveis() {
        return livroRepository.findLivrosDisponiveis().stream()
                .map(LivroDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LivroDTO> buscarPorTermo(String termo, Pageable pageable) {
        return livroRepository.buscarPorTermo(termo, pageable)
                .map(LivroDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarDisponiveis() {
        return livroRepository.countLivrosDisponiveis();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarIndisponiveis() {
        return livroRepository.countLivrosIndisponiveis();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isbnExiste(String isbn) {
        return livroRepository.existsByIsbn(isbn);
    }
}
