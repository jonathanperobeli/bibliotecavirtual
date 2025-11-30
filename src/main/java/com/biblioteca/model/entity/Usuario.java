package com.biblioteca.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entidade que representa um usuário do sistema.
 * Implementa UserDetails para integração com Spring Security.
 * 
 * Princípios aplicados:
 * - SRP: Responsabilidade única de representar dados do usuário
 * - OCP: Extensível através de herança se necessário
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(length = 20)
    private String telefone;

    @Column(length = 200)
    private String endereco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean ativo;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(name = "ultimo_acesso")
    private LocalDateTime ultimoAcesso;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Emprestimo> emprestimos = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
        ativo = true;
        if (role == null) {
            role = Role.USER;
        }
    }

    // Implementação de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return ativo;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }

    /**
     * Enum que define os papéis de usuário no sistema.
     */
    public enum Role {
        ADMIN,  // Administrador com acesso total
        USER    // Usuário comum
    }
}
