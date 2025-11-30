package com.biblioteca.service.impl;

import com.biblioteca.model.dto.DashboardDTO;
import com.biblioteca.model.entity.Emprestimo;
import com.biblioteca.model.entity.Livro;
import com.biblioteca.model.entity.Usuario;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.LivroRepository;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.service.ReportService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementação do serviço de geração de relatórios em PDF
 * Utiliza a biblioteca iTextPDF para geração dos documentos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {
    
    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    
    // Fontes padrão
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.GRAY);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY);
    
    // Cores
    private static final BaseColor PRIMARY_COLOR = new BaseColor(44, 62, 80);
    private static final BaseColor SECONDARY_COLOR = new BaseColor(52, 152, 219);
    
    @Override
    public ByteArrayOutputStream gerarRelatorioLivros() {
        log.info("Gerando relatório de livros");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();
            
            // Cabeçalho
            addHeader(document, "Relatório de Livros");
            
            // Tabela de livros
            List<Livro> livros = livroRepository.findAll();
            
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20);
            table.setWidths(new float[]{3, 2, 2, 1, 1});
            
            // Headers
            addTableHeader(table, "Título", "Autor(es)", "Categoria", "Total", "Disp.");
            
            // Dados
            for (Livro livro : livros) {
                table.addCell(createCell(livro.getTitulo()));
                String autores = livro.getAutores().isEmpty() ? "-" : 
                    livro.getAutores().stream()
                        .map(a -> a.getNome())
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("-");
                table.addCell(createCell(autores));
                table.addCell(createCell(livro.getCategoria() != null ? livro.getCategoria().getNome() : "-"));
                table.addCell(createCell(String.valueOf(livro.getQuantidadeTotal())));
                table.addCell(createCell(String.valueOf(livro.getQuantidadeDisponivel())));
            }
            
            document.add(table);
            
            // Rodapé com estatísticas
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Total de livros: " + livros.size(), NORMAL_FONT));
            document.add(new Paragraph("Livros disponíveis: " + 
                livros.stream().filter(Livro::isDisponivel).count(), NORMAL_FONT));
            
            addFooter(document);
            document.close();
            
        } catch (Exception e) {
            log.error("Erro ao gerar relatório de livros", e);
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
        
        return baos;
    }
    
    @Override
    public ByteArrayOutputStream gerarRelatorioEmprestimos() {
        log.info("Gerando relatório de empréstimos");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, baos);
            document.open();
            
            addHeader(document, "Relatório de Empréstimos");
            
            List<Emprestimo> emprestimos = emprestimoRepository.findAll();
            
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20);
            table.setWidths(new float[]{1, 3, 2, 2, 2, 1.5f, 1.5f});
            
            addTableHeader(table, "ID", "Livro", "Usuário", "Data Emp.", "Prev. Dev.", "Status", "Multa");
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (Emprestimo emp : emprestimos) {
                table.addCell(createCell(String.valueOf(emp.getId())));
                table.addCell(createCell(emp.getLivro().getTitulo()));
                table.addCell(createCell(emp.getUsuario().getNome()));
                table.addCell(createCell(emp.getDataEmprestimo().format(formatter)));
                table.addCell(createCell(emp.getDataPrevistaDevolucao().format(formatter)));
                
                PdfPCell statusCell = createCell(emp.getStatus().name());
                if (emp.getStatus() == Emprestimo.StatusEmprestimo.ATRASADO) {
                    statusCell.setBackgroundColor(new BaseColor(255, 200, 200));
                } else if (emp.getStatus() == Emprestimo.StatusEmprestimo.DEVOLVIDO) {
                    statusCell.setBackgroundColor(new BaseColor(200, 255, 200));
                }
                table.addCell(statusCell);
                
                java.math.BigDecimal multa = emp.getMulta();
                table.addCell(createCell(multa != null && multa.compareTo(java.math.BigDecimal.ZERO) > 0 ? 
                    String.format("R$ %.2f", multa) : "-"));
            }
            
            document.add(table);
            
            // Estatísticas
            document.add(new Paragraph("\n"));
            long ativos = emprestimos.stream()
                .filter(e -> e.getStatus() == Emprestimo.StatusEmprestimo.ATIVO).count();
            long atrasados = emprestimos.stream()
                .filter(e -> e.getStatus() == Emprestimo.StatusEmprestimo.ATRASADO).count();
            java.math.BigDecimal totalMultas = emprestimos.stream()
                .map(Emprestimo::getMulta)
                .filter(m -> m != null)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            
            document.add(new Paragraph("Total de empréstimos: " + emprestimos.size(), NORMAL_FONT));
            document.add(new Paragraph("Ativos: " + ativos, NORMAL_FONT));
            document.add(new Paragraph("Atrasados: " + atrasados, NORMAL_FONT));
            document.add(new Paragraph(String.format("Total em multas: R$ %.2f", totalMultas), NORMAL_FONT));
            
            addFooter(document);
            document.close();
            
        } catch (Exception e) {
            log.error("Erro ao gerar relatório de empréstimos", e);
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
        
        return baos;
    }
    
    @Override
    public ByteArrayOutputStream gerarRelatorioEmprestimosAtrasados() {
        log.info("Gerando relatório de empréstimos atrasados");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();
            
            addHeader(document, "Relatório de Empréstimos Atrasados");
            
            // Alerta
            Paragraph alerta = new Paragraph("⚠ EMPRÉSTIMOS COM ATRASO", 
                new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.RED));
            alerta.setAlignment(Element.ALIGN_CENTER);
            alerta.setSpacingAfter(20);
            document.add(alerta);
            
            List<Emprestimo> atrasados = emprestimoRepository.findEmprestimosAtrasados(LocalDate.now());
            
            if (atrasados.isEmpty()) {
                document.add(new Paragraph("Nenhum empréstimo atrasado encontrado.", NORMAL_FONT));
            } else {
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{3, 2, 2, 2, 2});
                
                addTableHeader(table, "Livro", "Usuário", "Email", "Prev. Dev.", "Multa");
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                
                for (Emprestimo emp : atrasados) {
                    table.addCell(createCell(emp.getLivro().getTitulo()));
                    table.addCell(createCell(emp.getUsuario().getNome()));
                    table.addCell(createCell(emp.getUsuario().getEmail()));
                    table.addCell(createCell(emp.getDataPrevistaDevolucao().format(formatter)));
                    java.math.BigDecimal multa = emp.getMulta() != null ? emp.getMulta() : emp.calcularMulta();
                    table.addCell(createCell(String.format("R$ %.2f", multa)));
                }
                
                document.add(table);
                
                java.math.BigDecimal totalMultas = atrasados.stream()
                    .map(e -> e.getMulta() != null ? e.getMulta() : e.calcularMulta())
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
                document.add(new Paragraph("\n"));
                document.add(new Paragraph("Total de empréstimos atrasados: " + atrasados.size(), NORMAL_FONT));
                document.add(new Paragraph(String.format("Total em multas pendentes: R$ %.2f", totalMultas), 
                    new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.RED)));
            }
            
            addFooter(document);
            document.close();
            
        } catch (Exception e) {
            log.error("Erro ao gerar relatório de empréstimos atrasados", e);
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
        
        return baos;
    }
    
    @Override
    public ByteArrayOutputStream gerarRelatorioUsuarios() {
        log.info("Gerando relatório de usuários");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();
            
            addHeader(document, "Relatório de Usuários");
            
            List<Usuario> usuarios = usuarioRepository.findAll();
            
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20);
            table.setWidths(new float[]{2.5f, 3, 1.5f, 1.5f, 1.5f});
            
            addTableHeader(table, "Nome", "Email", "Papel", "Status", "Empréstimos");
            
            for (Usuario usuario : usuarios) {
                table.addCell(createCell(usuario.getNome()));
                table.addCell(createCell(usuario.getEmail()));
                table.addCell(createCell(usuario.getRole().name()));
                
                PdfPCell statusCell = createCell(usuario.isAtivo() ? "Ativo" : "Inativo");
                statusCell.setBackgroundColor(usuario.isAtivo() ? 
                    new BaseColor(200, 255, 200) : new BaseColor(255, 200, 200));
                table.addCell(statusCell);
                
                table.addCell(createCell(String.valueOf(usuario.getEmprestimos().size())));
            }
            
            document.add(table);
            
            // Estatísticas
            document.add(new Paragraph("\n"));
            long ativos = usuarios.stream().filter(Usuario::isAtivo).count();
            long admins = usuarios.stream().filter(u -> u.getRole() == Usuario.Role.ADMIN).count();
            
            document.add(new Paragraph("Total de usuários: " + usuarios.size(), NORMAL_FONT));
            document.add(new Paragraph("Usuários ativos: " + ativos, NORMAL_FONT));
            document.add(new Paragraph("Administradores: " + admins, NORMAL_FONT));
            
            addFooter(document);
            document.close();
            
        } catch (Exception e) {
            log.error("Erro ao gerar relatório de usuários", e);
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
        
        return baos;
    }
    
    @Override
    public ByteArrayOutputStream gerarRelatorioDashboard(DashboardDTO dashboard) {
        log.info("Gerando relatório do dashboard");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();
            
            addHeader(document, "Relatório Geral - Dashboard");
            
            // Seção de Estatísticas Gerais
            document.add(new Paragraph("Estatísticas Gerais", SUBTITLE_FONT));
            document.add(new Paragraph("\n"));
            
            PdfPTable statsTable = new PdfPTable(2);
            statsTable.setWidthPercentage(60);
            statsTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            
            addStatRow(statsTable, "Total de Livros", String.valueOf(dashboard.getTotalLivros()));
            addStatRow(statsTable, "Livros Disponíveis", String.valueOf(dashboard.getLivrosDisponiveis()));
            addStatRow(statsTable, "Livros Indisponíveis", String.valueOf(dashboard.getLivrosIndisponiveis()));
            addStatRow(statsTable, "Total de Usuários", String.valueOf(dashboard.getTotalUsuarios()));
            addStatRow(statsTable, "Total de Autores", String.valueOf(dashboard.getTotalAutores()));
            addStatRow(statsTable, "Total de Categorias", String.valueOf(dashboard.getTotalCategorias()));
            addStatRow(statsTable, "Empréstimos Ativos", String.valueOf(dashboard.getEmprestimosAtivos()));
            addStatRow(statsTable, "Empréstimos Atrasados", String.valueOf(dashboard.getEmprestimosAtrasados()));
            
            document.add(statsTable);
            document.add(new Paragraph("\n\n"));
            
            // Taxa de ocupação
            if (dashboard.getTotalLivros() > 0) {
                double taxaOcupacao = ((double) dashboard.getLivrosIndisponiveis() / dashboard.getTotalLivros()) * 100;
                document.add(new Paragraph(String.format("Taxa de ocupação do acervo: %.1f%%", taxaOcupacao), NORMAL_FONT));
            }
            
            addFooter(document);
            document.close();
            
        } catch (Exception e) {
            log.error("Erro ao gerar relatório do dashboard", e);
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
        
        return baos;
    }
    
    // Métodos auxiliares
    
    private void addHeader(Document document, String title) throws DocumentException {
        Paragraph header = new Paragraph("BIBLIOTECA DIGITAL", TITLE_FONT);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
        
        Paragraph subtitle = new Paragraph(title, SUBTITLE_FONT);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(10);
        document.add(subtitle);
        
        // Linha separadora
        LineSeparator line = new LineSeparator();
        line.setLineColor(SECONDARY_COLOR);
        document.add(new Chunk(line));
    }
    
    private void addFooter(Document document) throws DocumentException {
        document.add(new Paragraph("\n\n"));
        
        LineSeparator line = new LineSeparator();
        line.setLineColor(BaseColor.LIGHT_GRAY);
        document.add(new Chunk(line));
        
        String dataGeracao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        Paragraph footer = new Paragraph("Relatório gerado em: " + dataGeracao, SMALL_FONT);
        footer.setAlignment(Element.ALIGN_RIGHT);
        document.add(footer);
        
        Paragraph system = new Paragraph("Sistema de Biblioteca Digital", SMALL_FONT);
        system.setAlignment(Element.ALIGN_RIGHT);
        document.add(system);
    }
    
    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setBackgroundColor(PRIMARY_COLOR);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            table.addCell(cell);
        }
    }
    
    private PdfPCell createCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, NORMAL_FONT));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }
    
    private void addStatRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, NORMAL_FONT));
        labelCell.setPadding(8);
        labelCell.setBackgroundColor(new BaseColor(245, 245, 245));
        table.addCell(labelCell);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(value, 
            new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD)));
        valueCell.setPadding(8);
        valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(valueCell);
    }
}
