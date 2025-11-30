package com.biblioteca.pattern.observer;

import com.biblioteca.model.entity.Emprestimo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Observer concreto para notificação por email (simulado).
 * Em produção, integraria com serviço de email real.
 */
@Component
public class EmailNotificationObserver implements EmprestimoObserver {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationObserver.class);

    @Override
    public void onEmprestimoRealizado(Emprestimo emprestimo) {
        enviarEmail(
                emprestimo.getUsuario().getEmail(),
                "Confirmação de Empréstimo",
                String.format("Olá %s,\n\nSeu empréstimo do livro '%s' foi realizado com sucesso.\n" +
                              "Data prevista para devolução: %s\n\nBoa leitura!",
                        emprestimo.getUsuario().getNome(),
                        emprestimo.getLivro().getTitulo(),
                        emprestimo.getDataPrevistaDevolucao())
        );
    }

    @Override
    public void onDevolucaoRealizada(Emprestimo emprestimo) {
        String corpo = String.format("Olá %s,\n\nA devolução do livro '%s' foi registrada.",
                emprestimo.getUsuario().getNome(),
                emprestimo.getLivro().getTitulo());
        
        if (emprestimo.getMulta() != null && emprestimo.getMulta().doubleValue() > 0) {
            corpo += String.format("\n\nMulta por atraso: R$ %.2f", emprestimo.getMulta());
        }
        
        enviarEmail(emprestimo.getUsuario().getEmail(), "Devolução Confirmada", corpo);
    }

    @Override
    public void onEmprestimoProximoVencimento(Emprestimo emprestimo) {
        enviarEmail(
                emprestimo.getUsuario().getEmail(),
                "⚠️ Lembrete: Devolução Próxima",
                String.format("Olá %s,\n\nLembramos que o livro '%s' deve ser devolvido em %s.\n" +
                              "Evite multas, devolva no prazo!",
                        emprestimo.getUsuario().getNome(),
                        emprestimo.getLivro().getTitulo(),
                        emprestimo.getDataPrevistaDevolucao())
        );
    }

    @Override
    public void onEmprestimoAtrasado(Emprestimo emprestimo) {
        enviarEmail(
                emprestimo.getUsuario().getEmail(),
                "🚨 URGENTE: Livro em Atraso",
                String.format("Olá %s,\n\nO livro '%s' está com %d dias de atraso!\n" +
                              "Por favor, devolva o mais rápido possível para evitar multas adicionais.",
                        emprestimo.getUsuario().getNome(),
                        emprestimo.getLivro().getTitulo(),
                        emprestimo.getDiasAtraso())
        );
    }

    /**
     * Simula envio de email (em produção, usar JavaMailSender ou similar).
     */
    private void enviarEmail(String destinatario, String assunto, String corpo) {
        logger.info("📧 [EMAIL SIMULADO] Para: {} | Assunto: {}", destinatario, assunto);
        logger.debug("Corpo do email:\n{}", corpo);
    }
}
