package com.biblioteca.pattern.strategy;

import com.biblioteca.model.entity.Emprestimo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Estratégia de multa fixa por dia de atraso.
 */
@Component("multaFixa")
public class MultaFixaStrategy implements MultaStrategy {

    private static final BigDecimal VALOR_POR_DIA = new BigDecimal("2.00");

    @Override
    public BigDecimal calcularMulta(Emprestimo emprestimo) {
        long diasAtraso = emprestimo.getDiasAtraso();
        if (diasAtraso <= 0) {
            return BigDecimal.ZERO;
        }
        return VALOR_POR_DIA.multiply(BigDecimal.valueOf(diasAtraso));
    }

    @Override
    public String getDescricao() {
        return "Multa fixa de R$ " + VALOR_POR_DIA + " por dia de atraso";
    }
}
