package com.biblioteca.service.impl;

import com.biblioteca.exception.BusinessException;
import com.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.model.dto.UsuarioDTO;
import com.biblioteca.model.entity.Usuario;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Usuário.
 * 
 * Princípios SOLID aplicados:
 * - SRP: Responsabilidade única de gerenciar usuários
 * - OCP: Aberto para extensão através da interface
 * - DIP: Depende de abstrações (interfaces)
 */
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarEntidadePorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public UsuarioDTO salvar(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + usuarioDTO.getEmail());
        }

        Usuario usuario = usuarioDTO.toEntity();
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        Usuario salvo = usuarioRepository.save(usuario);
        return UsuarioDTO.fromEntity(salvo);
    }

    @Override
    public UsuarioDTO atualizar(Long id, UsuarioDTO usuarioDTO) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));

        // Verificar se novo email já existe (se for diferente do atual)
        if (!existente.getEmail().equals(usuarioDTO.getEmail()) 
                && usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + usuarioDTO.getEmail());
        }

        existente.setNome(usuarioDTO.getNome());
        existente.setEmail(usuarioDTO.getEmail());
        existente.setTelefone(usuarioDTO.getTelefone());
        existente.setEndereco(usuarioDTO.getEndereco());

        // Atualiza senha apenas se fornecida
        if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
            existente.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        }

        // Atualiza role apenas se fornecido
        if (usuarioDTO.getRole() != null) {
            existente.setRole(usuarioDTO.getRole());
        }

        // Atualiza status ativo
        if (usuarioDTO.getAtivo() != null) {
            existente.setAtivo(usuarioDTO.getAtivo());
        }

        Usuario atualizado = usuarioRepository.save(existente);
        return UsuarioDTO.fromEntity(atualizado);
    }

    @Override
    public void deletar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
        
        // Não permitir excluir o admin principal
        if ("admin@biblioteca.com".equals(usuario.getEmail())) {
            throw new BusinessException("Não é possível excluir o usuário administrador principal!");
        }
        
        try {
            usuarioRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Não é possível excluir usuário com empréstimos ativos!");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarPorTermo(String termo) {
        return usuarioRepository.buscarPorTermo(termo).stream()
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarUsuariosAtivos() {
        return usuarioRepository.countUsuariosAtivos();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
