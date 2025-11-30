package com.biblioteca.service.impl;

import com.biblioteca.exception.BusinessException;
import com.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.model.dto.CategoriaDTO;
import com.biblioteca.model.entity.Categoria;
import com.biblioteca.repository.CategoriaRepository;
import com.biblioteca.service.CategoriaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Categoria.
 */
@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaDTO> buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .map(CategoriaDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarEntidadePorId(Long id) {
        return categoriaRepository.findById(id);
    }

    @Override
    public CategoriaDTO salvar(CategoriaDTO categoriaDTO) {
        if (categoriaRepository.existsByNome(categoriaDTO.getNome())) {
            throw new BusinessException("Categoria já existe: " + categoriaDTO.getNome());
        }

        Categoria categoria = categoriaDTO.toEntity();
        Categoria salva = categoriaRepository.save(categoria);
        return CategoriaDTO.fromEntity(salva);
    }

    @Override
    public CategoriaDTO atualizar(Long id, CategoriaDTO categoriaDTO) {
        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));

        // Verificar se novo nome já existe
        if (!existente.getNome().equals(categoriaDTO.getNome()) 
                && categoriaRepository.existsByNome(categoriaDTO.getNome())) {
            throw new BusinessException("Categoria já existe: " + categoriaDTO.getNome());
        }

        existente.setNome(categoriaDTO.getNome());
        existente.setDescricao(categoriaDTO.getDescricao());

        Categoria atualizada = categoriaRepository.save(existente);
        return CategoriaDTO.fromEntity(atualizada);
    }

    @Override
    public void deletar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));

        if (!categoria.getLivros().isEmpty()) {
            throw new BusinessException("Não é possível excluir categoria com livros associados");
        }

        categoriaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> buscarPorNome(String nome) {
        return categoriaRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(CategoriaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean nomeExiste(String nome) {
        return categoriaRepository.existsByNome(nome);
    }
}
