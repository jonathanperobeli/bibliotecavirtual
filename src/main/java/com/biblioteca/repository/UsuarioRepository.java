package com.biblioteca.repository;

import com.biblioteca.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de persistência de Usuário.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findByAtivoTrue();

    List<Usuario> findByRole(Usuario.Role role);

    @Query("SELECT u FROM Usuario u WHERE u.nome LIKE %:termo% OR u.email LIKE %:termo%")
    List<Usuario> buscarPorTermo(@Param("termo") String termo);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.ativo = true")
    Long countUsuariosAtivos();
}
