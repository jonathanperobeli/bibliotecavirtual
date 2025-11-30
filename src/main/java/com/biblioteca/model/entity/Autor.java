package com.biblioteca.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidade que representa um Autor de livros.
 * 
 * Relacionamento Many-to-Many com Livro.
 */
@Entity
@Table(name = "autores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 100)
    private String nacionalidade;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(columnDefinition = "TEXT")
    private String biografia;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.LAZY)
    private Set<Livro> livros = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autor autor = (Autor) o;
        return id != null && id.equals(autor.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
