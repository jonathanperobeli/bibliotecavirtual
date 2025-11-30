package com.biblioteca.pattern.factory;

import com.biblioteca.model.entity.Emprestimo;
import com.biblioteca.model.entity.Livro;
import com.biblioteca.model.entity.Usuario;

import java.time.LocalDate;

/**
 * PADRÃO DE PROJETO: FACTORY METHOD
 * 
 * Este padrão é usado para criar objetos Emprestimo de forma encapsulada,
 * permitindo diferentes tipos de empréstimos com regras específicas.
 * 
 * Benefícios:
 * - Encapsula a lógica de criação de empréstimos
 * - Facilita a adição de novos tipos de empréstimos
 * - Centraliza validações e regras de negócio
 */
public abstract class EmprestimoFactory {

    /**
     * Factory Method para criar empréstimos.
     */
    public abstract Emprestimo criarEmprestimo(Usuario usuario, Livro livro);

    /**
     * Template method com validações comuns.
     */
    protected void validarPreCondicoes(Usuario usuario, Livro livro) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo");
        }
        if (!usuario.isAtivo()) {
            throw new IllegalStateException("Usuário inativo não pode realizar empréstimos");
        }
        if (!livro.isDisponivel()) {
            throw new IllegalStateException("Livro não está disponível para empréstimo");
        }
    }
}
