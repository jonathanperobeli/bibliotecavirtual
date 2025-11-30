package com.biblioteca.repository;

import com.biblioteca.model.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de persistência de Categoria.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNome(String nome);

    List<Categoria> findByNomeContainingIgnoreCase(String nome);

    boolean existsByNome(String nome);

    @Query("SELECT c FROM Categoria c LEFT JOIN FETCH c.livros ORDER BY c.nome")
    List<Categoria> findAllWithLivros();

    @Query("SELECT c, COUNT(l) FROM Categoria c LEFT JOIN c.livros l GROUP BY c ORDER BY COUNT(l) DESC")
    List<Object[]> findCategoriasComContagem();
}
