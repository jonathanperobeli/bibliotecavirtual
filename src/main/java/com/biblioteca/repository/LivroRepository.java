package com.biblioteca.repository;

import com.biblioteca.model.entity.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de persistência de Livro.
 */
@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    Optional<Livro> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    List<Livro> findByStatus(Livro.StatusLivro status);

    List<Livro> findByCategoriaId(Long categoriaId);

    @Query("SELECT l FROM Livro l WHERE l.quantidadeDisponivel > 0")
    List<Livro> findLivrosDisponiveis();

    @Query("SELECT l FROM Livro l WHERE l.quantidadeDisponivel = 0")
    List<Livro> findLivrosIndisponiveis();

    @Query("SELECT l FROM Livro l JOIN l.autores a WHERE a.id = :autorId")
    List<Livro> findByAutorId(@Param("autorId") Long autorId);

    @Query("SELECT l FROM Livro l WHERE " +
           "LOWER(l.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(l.isbn) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(l.editora) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Livro> buscarPorTermo(@Param("termo") String termo, Pageable pageable);

    @Query("SELECT COUNT(l) FROM Livro l WHERE l.quantidadeDisponivel > 0")
    Long countLivrosDisponiveis();

    @Query("SELECT COUNT(l) FROM Livro l WHERE l.quantidadeDisponivel = 0")
    Long countLivrosIndisponiveis();

    @Query("SELECT l FROM Livro l LEFT JOIN FETCH l.categoria LEFT JOIN FETCH l.autores WHERE l.id = :id")
    Optional<Livro> findByIdWithRelations(@Param("id") Long id);
}
