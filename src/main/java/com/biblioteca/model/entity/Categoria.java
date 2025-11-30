package com.biblioteca.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidade que representa uma Categoria de livros.
 * 
 * Relacionamento One-to-Many com Livro.
 */
@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private Set<Livro> livros = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return id != null && id.equals(categoria.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
