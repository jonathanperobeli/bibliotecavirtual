package com.biblioteca.service;

import com.biblioteca.model.dto.AutorDTO;
import com.biblioteca.model.entity.Autor;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço para operações com Autor.
 */
public interface AutorService {

    List<AutorDTO> listarTodos();

    Optional<AutorDTO> buscarPorId(Long id);

    Optional<Autor> buscarEntidadePorId(Long id);

    AutorDTO salvar(AutorDTO autorDTO);

    AutorDTO atualizar(Long id, AutorDTO autorDTO);

    void deletar(Long id);

    List<AutorDTO> buscarPorNome(String nome);

    List<String> listarNacionalidades();
}
