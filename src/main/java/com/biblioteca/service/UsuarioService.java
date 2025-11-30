package com.biblioteca.service;

import com.biblioteca.model.dto.UsuarioDTO;
import com.biblioteca.model.entity.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço para operações com Usuário.
 * 
 * Princípio aplicado: ISP (Interface Segregation Principle)
 */
public interface UsuarioService {

    List<UsuarioDTO> listarTodos();

    Optional<UsuarioDTO> buscarPorId(Long id);

    Optional<Usuario> buscarEntidadePorId(Long id);

    Optional<Usuario> buscarPorEmail(String email);

    UsuarioDTO salvar(UsuarioDTO usuarioDTO);

    UsuarioDTO atualizar(Long id, UsuarioDTO usuarioDTO);

    void deletar(Long id);

    List<UsuarioDTO> buscarPorTermo(String termo);

    Long contarUsuariosAtivos();

    boolean emailExiste(String email);
}
