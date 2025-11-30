package com.biblioteca.repository;

import com.biblioteca.model.entity.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositório para operações de persistência de Emprestimo.
 */
@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByUsuarioId(Long usuarioId);

    List<Emprestimo> findByLivroId(Long livroId);

    List<Emprestimo> findByStatus(Emprestimo.StatusEmprestimo status);

    @Query("SELECT e FROM Emprestimo e WHERE e.status = 'ATIVO'")
    List<Emprestimo> findEmprestimosAtivos();

    @Query("SELECT e FROM Emprestimo e WHERE e.status = 'ATIVO' AND e.dataPrevistaDevolucao < :hoje")
    List<Emprestimo> findEmprestimosAtrasados(@Param("hoje") LocalDate hoje);

    @Query("SELECT e FROM Emprestimo e WHERE e.usuario.id = :usuarioId AND e.status = 'ATIVO'")
    List<Emprestimo> findEmprestimosAtivosPorUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT COUNT(e) FROM Emprestimo e WHERE e.status = 'ATIVO'")
    Long countEmprestimosAtivos();

    @Query("SELECT COUNT(e) FROM Emprestimo e WHERE e.status = 'ATIVO' AND e.dataPrevistaDevolucao < :hoje")
    Long countEmprestimosAtrasados(@Param("hoje") LocalDate hoje);

    @Query("SELECT e FROM Emprestimo e LEFT JOIN FETCH e.usuario LEFT JOIN FETCH e.livro WHERE e.id = :id")
    Emprestimo findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT e FROM Emprestimo e WHERE e.dataEmprestimo BETWEEN :inicio AND :fim")
    List<Emprestimo> findByPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT e FROM Emprestimo e WHERE e.usuario.id = :usuarioId AND e.livro.id = :livroId AND e.status = 'ATIVO'")
    List<Emprestimo> findEmprestimoAtivo(@Param("usuarioId") Long usuarioId, @Param("livroId") Long livroId);
}
