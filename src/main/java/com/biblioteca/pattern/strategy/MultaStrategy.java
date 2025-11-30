package com.biblioteca.pattern.strategy;

import com.biblioteca.model.entity.Emprestimo;

import java.math.BigDecimal;

/**
 * PADRÃO DE PROJETO: STRATEGY
 * 
 * Interface que define a estratégia de cálculo de multa.
 * Permite diferentes algoritmos de cálculo intercambiáveis.
 * 
 * Benefícios:
 * - Separa o algoritmo de cálculo do contexto
 * - Facilita adição de novas estratégias
 * - Permite trocar estratégia em tempo de execução
 */
public interface MultaStrategy {

    /**
     * Calcula a multa para um empréstimo.
     * 
     * @param emprestimo o empréstimo a ser calculado
     * @return valor da multa
     */
    BigDecimal calcularMulta(Emprestimo emprestimo);

    /**
     * Retorna descrição da estratégia.
     */
    String getDescricao();
}
