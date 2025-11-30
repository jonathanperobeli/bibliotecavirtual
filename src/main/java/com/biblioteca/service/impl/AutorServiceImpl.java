package com.biblioteca.service.impl;

import com.biblioteca.exception.BusinessException;
import com.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.model.dto.AutorDTO;
import com.biblioteca.model.entity.Autor;
import com.biblioteca.repository.AutorRepository;
import com.biblioteca.service.AutorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Autor.
 */
@Service
@Transactional
public class AutorServiceImpl implements AutorService {

    private final AutorRepository autorRepository;

    public AutorServiceImpl(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutorDTO> listarTodos() {
        return autorRepository.findAll().stream()
                .map(AutorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AutorDTO> buscarPorId(Long id) {
        return autorRepository.findById(id)
                .map(AutorDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Autor> buscarEntidadePorId(Long id) {
        return autorRepository.findById(id);
    }

    @Override
    public AutorDTO salvar(AutorDTO autorDTO) {
        Autor autor = autorDTO.toEntity();
        Autor salvo = autorRepository.save(autor);
        return AutorDTO.fromEntity(salvo);
    }

    @Override
    public AutorDTO atualizar(Long id, AutorDTO autorDTO) {
        Autor existente = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor", id));

        existente.setNome(autorDTO.getNome());
        existente.setNacionalidade(autorDTO.getNacionalidade());
        existente.setDataNascimento(autorDTO.getDataNascimento());
        existente.setBiografia(autorDTO.getBiografia());

        Autor atualizado = autorRepository.save(existente);
        return AutorDTO.fromEntity(atualizado);
    }

    @Override
    public void deletar(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor", id));

        if (!autor.getLivros().isEmpty()) {
            throw new BusinessException("Não é possível excluir autor com livros associados");
        }

        autorRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutorDTO> buscarPorNome(String nome) {
        return autorRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(AutorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> listarNacionalidades() {
        return autorRepository.findAllNacionalidades();
    }
}
