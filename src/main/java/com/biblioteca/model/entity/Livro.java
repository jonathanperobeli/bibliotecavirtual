package com.biblioteca.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidade que representa um Livro da biblioteca.
 * 
 * Relacionamentos:
 * - Many-to-Many com Autor
 * - Many-to-One com Categoria
 * - One-to-Many com Emprestimo
 */
@Entity
@Table(name = "livros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(unique = true, length = 20)
    private String isbn;

    @Column(name = "ano_publicacao")
    private Integer anoPublicacao;

    @Column(length = 100)
    private String editora;

    @Column(length = 50)
    private String edicao;

    @Column(name = "numero_paginas")
    private Integer numeroPaginas;

    @Column(columnDefinition = "TEXT")
    private String sinopse;

    @Column(name = "quantidade_total", nullable = false)
    private Integer quantidadeTotal;

    @Column(name = "quantidade_disponivel", nullable = false)
    private Integer quantidadeDisponivel;

    @Column(name = "url_capa")
    private String urlCapa;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusLivro status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "livro_autor",
        joinColumns = @JoinColumn(name = "livro_id"),
        inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores = new HashSet<>();

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Emprestimo> emprestimos = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDate.now();
        if (status == null) {
            status = StatusLivro.DISPONIVEL;
        }
        if (quantidadeDisponivel == null) {
            quantidadeDisponivel = quantidadeTotal;
        }
    }

    /**
     * Verifica se o livro está disponível para empréstimo.
     */
    public boolean isDisponivel() {
        return quantidadeDisponivel > 0 && status == StatusLivro.DISPONIVEL;
    }

    /**
     * Realiza o empréstimo de uma unidade do livro.
     */
    public void emprestar() {
        if (isDisponivel()) {
            quantidadeDisponivel--;
            if (quantidadeDisponivel == 0) {
                status = StatusLivro.INDISPONIVEL;
            }
        }
    }

    /**
     * Realiza a devolução de uma unidade do livro.
     */
    public void devolver() {
        quantidadeDisponivel++;
        if (quantidadeDisponivel > 0) {
            status = StatusLivro.DISPONIVEL;
        }
    }

    /**
     * Enum que define os status possíveis de um livro.
     */
    public enum StatusLivro {
        DISPONIVEL,
        INDISPONIVEL,
        EM_MANUTENCAO,
        DESCARTADO
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livro livro = (Livro) o;
        return id != null && id.equals(livro.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
