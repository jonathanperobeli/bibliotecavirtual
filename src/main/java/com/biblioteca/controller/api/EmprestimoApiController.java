package com.biblioteca.controller.api;

import com.biblioteca.model.dto.EmprestimoDTO;
import com.biblioteca.service.EmprestimoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller REST para operações com Empréstimos.
 */
@RestController
@RequestMapping("/api/emprestimos")
@Tag(name = "Empréstimos", description = "API para gerenciamento de empréstimos de livros")
public class EmprestimoApiController {

    private final EmprestimoService emprestimoService;

    public EmprestimoApiController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos os empréstimos", description = "Retorna uma lista com todos os empréstimos")
    @ApiResponse(responseCode = "200", description = "Lista de empréstimos retornada com sucesso")
    public ResponseEntity<List<EmprestimoDTO>> listarTodos() {
        return ResponseEntity.ok(emprestimoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar empréstimo por ID", description = "Retorna um empréstimo específico pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empréstimo encontrado"),
        @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")
    })
    public ResponseEntity<EmprestimoDTO> buscarPorId(
            @Parameter(description = "ID do empréstimo") @PathVariable Long id) {
        return emprestimoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Realizar empréstimo", description = "Cria um novo empréstimo de livro")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Empréstimo realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Livro indisponível ou usuário atingiu limite")
    })
    public ResponseEntity<EmprestimoDTO> realizarEmprestimo(
            @Parameter(description = "ID do usuário") @RequestParam Long usuarioId,
            @Parameter(description = "ID do livro") @RequestParam Long livroId) {
        EmprestimoDTO emprestimo = emprestimoService.realizarEmprestimo(usuarioId, livroId);
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimo);
    }

    @PutMapping("/{id}/devolver")
    @Operation(summary = "Devolver livro", description = "Registra a devolução de um livro")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Devolução realizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado"),
        @ApiResponse(responseCode = "400", description = "Empréstimo já devolvido")
    })
    public ResponseEntity<EmprestimoDTO> realizarDevolucao(
            @Parameter(description = "ID do empréstimo") @PathVariable Long id) {
        EmprestimoDTO emprestimo = emprestimoService.realizarDevolucao(id);
        return ResponseEntity.ok(emprestimo);
    }

    @PutMapping("/{id}/renovar")
    @Operation(summary = "Renovar empréstimo", description = "Renova um empréstimo por mais um período")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Renovação realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Não é possível renovar")
    })
    public ResponseEntity<EmprestimoDTO> renovarEmprestimo(
            @Parameter(description = "ID do empréstimo") @PathVariable Long id) {
        EmprestimoDTO emprestimo = emprestimoService.renovarEmprestimo(id);
        return ResponseEntity.ok(emprestimo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cancelar empréstimo", description = "Cancela um empréstimo ativo")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Empréstimo cancelado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")
    })
    public ResponseEntity<Void> cancelarEmprestimo(
            @Parameter(description = "ID do empréstimo") @PathVariable Long id) {
        emprestimoService.cancelarEmprestimo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar empréstimos por usuário", description = "Retorna todos os empréstimos de um usuário")
    public ResponseEntity<List<EmprestimoDTO>> listarPorUsuario(
            @Parameter(description = "ID do usuário") @PathVariable Long usuarioId) {
        return ResponseEntity.ok(emprestimoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar empréstimos ativos", description = "Retorna todos os empréstimos em andamento")
    public ResponseEntity<List<EmprestimoDTO>> listarAtivos() {
        return ResponseEntity.ok(emprestimoService.listarAtivos());
    }

    @GetMapping("/atrasados")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar empréstimos atrasados", description = "Retorna todos os empréstimos com devolução atrasada")
    public ResponseEntity<List<EmprestimoDTO>> listarAtrasados() {
        return ResponseEntity.ok(emprestimoService.listarAtrasados());
    }

    @GetMapping("/periodo")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar empréstimos por período", description = "Retorna empréstimos realizados em um período")
    public ResponseEntity<List<EmprestimoDTO>> listarPorPeriodo(
            @Parameter(description = "Data inicial") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Data final") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(emprestimoService.listarPorPeriodo(inicio, fim));
    }
}
