package com.biblioteca.repository;

import com.biblioteca.model.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para operações de persistência de Autor.
 */
@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    List<Autor> findByNomeContainingIgnoreCase(String nome);

    List<Autor> findByNacionalidade(String nacionalidade);

    @Query("SELECT DISTINCT a.nacionalidade FROM Autor a WHERE a.nacionalidade IS NOT NULL ORDER BY a.nacionalidade")
    List<String> findAllNacionalidades();

    @Query("SELECT a FROM Autor a JOIN a.livros l WHERE l.id = :livroId")
    List<Autor> findByLivroId(@Param("livroId") Long livroId);

    boolean existsByNome(String nome);
}
