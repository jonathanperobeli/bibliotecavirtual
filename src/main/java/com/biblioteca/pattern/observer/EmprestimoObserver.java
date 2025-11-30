package com.biblioteca.pattern.observer;

import com.biblioteca.model.entity.Emprestimo;

/**
 * PADRÃO DE PROJETO: OBSERVER
 * 
 * Interface para observadores de eventos de empréstimo.
 * Permite que diferentes componentes reajam a eventos do sistema.
 * 
 * Benefícios:
 * - Desacoplamento entre componentes
 * - Facilita adição de novos comportamentos
 * - Comunicação one-to-many
 */
public interface EmprestimoObserver {

    /**
     * Chamado quando um novo empréstimo é realizado.
     */
    void onEmprestimoRealizado(Emprestimo emprestimo);

    /**
     * Chamado quando um livro é devolvido.
     */
    void onDevolucaoRealizada(Emprestimo emprestimo);

    /**
     * Chamado quando um empréstimo está próximo do vencimento.
     */
    void onEmprestimoProximoVencimento(Emprestimo emprestimo);

    /**
     * Chamado quando um empréstimo entra em atraso.
     */
    void onEmprestimoAtrasado(Emprestimo emprestimo);
}
