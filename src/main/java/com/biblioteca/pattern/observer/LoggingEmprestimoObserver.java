package com.biblioteca.pattern.observer;

import com.biblioteca.model.entity.Emprestimo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Observer concreto para logging de eventos de empréstimo.
 */
@Component
public class LoggingEmprestimoObserver implements EmprestimoObserver {

    private static final Logger logger = LoggerFactory.getLogger(LoggingEmprestimoObserver.class);

    @Override
    public void onEmprestimoRealizado(Emprestimo emprestimo) {
        logger.info("📚 Empréstimo realizado - Livro: '{}' | Usuário: {} | Data prevista: {}",
                emprestimo.getLivro().getTitulo(),
                emprestimo.getUsuario().getNome(),
                emprestimo.getDataPrevistaDevolucao());
    }

    @Override
    public void onDevolucaoRealizada(Emprestimo emprestimo) {
        String mensagem = emprestimo.getMulta() != null && emprestimo.getMulta().doubleValue() > 0
                ? String.format("com multa de R$ %.2f", emprestimo.getMulta())
                : "sem multa";
        
        logger.info("✅ Devolução realizada - Livro: '{}' | Usuário: {} | {}",
                emprestimo.getLivro().getTitulo(),
                emprestimo.getUsuario().getNome(),
                mensagem);
    }

    @Override
    public void onEmprestimoProximoVencimento(Emprestimo emprestimo) {
        logger.warn("⚠️ Empréstimo próximo do vencimento - Livro: '{}' | Usuário: {} | Vence em: {}",
                emprestimo.getLivro().getTitulo(),
                emprestimo.getUsuario().getNome(),
                emprestimo.getDataPrevistaDevolucao());
    }

    @Override
    public void onEmprestimoAtrasado(Emprestimo emprestimo) {
        logger.error("🚨 Empréstimo ATRASADO - Livro: '{}' | Usuário: {} | Dias de atraso: {}",
                emprestimo.getLivro().getTitulo(),
                emprestimo.getUsuario().getNome(),
                emprestimo.getDiasAtraso());
    }
}
