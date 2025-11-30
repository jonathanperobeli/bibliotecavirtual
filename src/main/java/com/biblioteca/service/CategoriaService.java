package com.biblioteca.service;

import com.biblioteca.model.dto.CategoriaDTO;
import com.biblioteca.model.entity.Categoria;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço para operações com Categoria.
 */
public interface CategoriaService {

    List<CategoriaDTO> listarTodas();

    Optional<CategoriaDTO> buscarPorId(Long id);

    Optional<Categoria> buscarEntidadePorId(Long id);

    CategoriaDTO salvar(CategoriaDTO categoriaDTO);

    CategoriaDTO atualizar(Long id, CategoriaDTO categoriaDTO);

    void deletar(Long id);

    List<CategoriaDTO> buscarPorNome(String nome);

    boolean nomeExiste(String nome);
}
