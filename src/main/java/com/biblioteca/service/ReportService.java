package com.biblioteca.service;

import com.biblioteca.model.dto.DashboardDTO;
import java.io.ByteArrayOutputStream;

/**
 * Interface para serviço de geração de relatórios
 */
public interface ReportService {
    
    /**
     * Gera relatório PDF de livros
     */
    ByteArrayOutputStream gerarRelatorioLivros();
    
    /**
     * Gera relatório PDF de empréstimos
     */
    ByteArrayOutputStream gerarRelatorioEmprestimos();
    
    /**
     * Gera relatório PDF de empréstimos atrasados
     */
    ByteArrayOutputStream gerarRelatorioEmprestimosAtrasados();
    
    /**
     * Gera relatório PDF de usuários
     */
    ByteArrayOutputStream gerarRelatorioUsuarios();
    
    /**
     * Gera relatório completo do dashboard
     */
    ByteArrayOutputStream gerarRelatorioDashboard(DashboardDTO dashboard);
}
