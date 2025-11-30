package com.biblioteca.pattern.strategy;

import com.biblioteca.model.entity.Emprestimo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Estratégia de multa progressiva - aumenta conforme dias de atraso.
 */
@Component("multaProgressiva")
public class MultaProgressivaStrategy implements MultaStrategy {

    private static final BigDecimal VALOR_BASE = new BigDecimal("1.00");
    private static final BigDecimal FATOR_PROGRESSIVO = new BigDecimal("0.50");
    private static final BigDecimal MULTA_MAXIMA_POR_DIA = new BigDecimal("10.00");

    @Override
    public BigDecimal calcularMulta(Emprestimo emprestimo) {
        long diasAtraso = emprestimo.getDiasAtraso();
        if (diasAtraso <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal multaTotal = BigDecimal.ZERO;
        
        for (int dia = 1; dia <= diasAtraso; dia++) {
            BigDecimal multaDia = VALOR_BASE.add(
                FATOR_PROGRESSIVO.multiply(BigDecimal.valueOf(dia - 1))
            );
            
            // Limita a multa máxima por dia
            if (multaDia.compareTo(MULTA_MAXIMA_POR_DIA) > 0) {
                multaDia = MULTA_MAXIMA_POR_DIA;
            }
            
            multaTotal = multaTotal.add(multaDia);
        }

        return multaTotal;
    }

    @Override
    public String getDescricao() {
        return "Multa progressiva: inicia em R$ " + VALOR_BASE + 
               " e aumenta R$ " + FATOR_PROGRESSIVO + " por dia";
    }
}
