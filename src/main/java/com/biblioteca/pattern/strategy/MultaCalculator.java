package com.biblioteca.pattern.strategy;

import com.biblioteca.model.entity.Emprestimo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Contexto que utiliza a estratégia de cálculo de multa.
 * Permite trocar a estratégia dinamicamente.
 */
@Component
public class MultaCalculator {

    private MultaStrategy strategy;

    public MultaCalculator(@Qualifier("multaFixa") MultaStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(MultaStrategy strategy) {
        this.strategy = strategy;
    }

    public BigDecimal calcular(Emprestimo emprestimo) {
        return strategy.calcularMulta(emprestimo);
    }

    public String getDescricaoEstrategia() {
        return strategy.getDescricao();
    }
}
