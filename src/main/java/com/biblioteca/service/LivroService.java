package com.biblioteca.service;

import com.biblioteca.model.dto.LivroDTO;
import com.biblioteca.model.entity.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço para operações com Livro.
 */
public interface LivroService {

    List<LivroDTO> listarTodos();

    Page<LivroDTO> listarPaginado(Pageable pageable);

    Optional<LivroDTO> buscarPorId(Long id);

    Optional<Livro> buscarEntidadePorId(Long id);

    LivroDTO salvar(LivroDTO livroDTO);

    LivroDTO atualizar(Long id, LivroDTO livroDTO);

    void deletar(Long id);

    List<LivroDTO> buscarPorTitulo(String titulo);

    List<LivroDTO> buscarPorCategoria(Long categoriaId);

    List<LivroDTO> buscarPorAutor(Long autorId);

    List<LivroDTO> listarDisponiveis();

    Page<LivroDTO> buscarPorTermo(String termo, Pageable pageable);

    Long contarDisponiveis();

    Long contarIndisponiveis();

    boolean isbnExiste(String isbn);
}
