package com.biblioteca.controller.web;

import com.biblioteca.model.dto.DashboardDTO;
import com.biblioteca.service.DashboardService;
import com.biblioteca.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;

/**
 * Controller para geração e download de relatórios PDF
 */
@Controller
@RequestMapping("/relatorios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {
    
    private final ReportService reportService;
    private final DashboardService dashboardService;
    
    @GetMapping("/livros")
    public ResponseEntity<byte[]> relatorioLivros() {
        ByteArrayOutputStream baos = reportService.gerarRelatorioLivros();
        return createPdfResponse(baos, "relatorio-livros.pdf");
    }
    
    @GetMapping("/emprestimos")
    public ResponseEntity<byte[]> relatorioEmprestimos() {
        ByteArrayOutputStream baos = reportService.gerarRelatorioEmprestimos();
        return createPdfResponse(baos, "relatorio-emprestimos.pdf");
    }
    
    @GetMapping("/emprestimos/atrasados")
    public ResponseEntity<byte[]> relatorioEmprestimosAtrasados() {
        ByteArrayOutputStream baos = reportService.gerarRelatorioEmprestimosAtrasados();
        return createPdfResponse(baos, "relatorio-emprestimos-atrasados.pdf");
    }
    
    @GetMapping("/usuarios")
    public ResponseEntity<byte[]> relatorioUsuarios() {
        ByteArrayOutputStream baos = reportService.gerarRelatorioUsuarios();
        return createPdfResponse(baos, "relatorio-usuarios.pdf");
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<byte[]> relatorioDashboard() {
        DashboardDTO dashboard = dashboardService.obterEstatisticas();
        ByteArrayOutputStream baos = reportService.gerarRelatorioDashboard(dashboard);
        return createPdfResponse(baos, "relatorio-dashboard.pdf");
    }
    
    private ResponseEntity<byte[]> createPdfResponse(ByteArrayOutputStream baos, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(baos.toByteArray());
    }
}
