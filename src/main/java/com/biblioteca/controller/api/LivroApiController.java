package com.biblioteca.controller.api;

import com.biblioteca.model.dto.LivroDTO;
import com.biblioteca.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações com Livros.
 */
@RestController
@RequestMapping("/api/livros")
@Tag(name = "Livros", description = "API para gerenciamento de livros")
public class LivroApiController {

    private final LivroService livroService;

    public LivroApiController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os livros", description = "Retorna uma lista paginada de todos os livros")
    @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso")
    public ResponseEntity<Page<LivroDTO>> listarTodos(
            @PageableDefault(size = 10, sort = "titulo") Pageable pageable) {
        return ResponseEntity.ok(livroService.listarPaginado(pageable));
    }

    @GetMapping("/todos")
    @Operation(summary = "Listar todos os livros sem paginação", description = "Retorna todos os livros cadastrados")
    public ResponseEntity<List<LivroDTO>> listarTodosSemPaginacao() {
        return ResponseEntity.ok(livroService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID", description = "Retorna um livro específico pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Livro encontrado"),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<LivroDTO> buscarPorId(
            @Parameter(description = "ID do livro") @PathVariable Long id) {
        return livroService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar novo livro", description = "Cadastra um novo livro no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Livro criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou ISBN já existe")
    })
    public ResponseEntity<LivroDTO> criar(@Valid @RequestBody LivroDTO livroDTO) {
        LivroDTO criado = livroService.salvar(livroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar livro", description = "Atualiza os dados de um livro existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<LivroDTO> atualizar(
            @Parameter(description = "ID do livro") @PathVariable Long id,
            @Valid @RequestBody LivroDTO livroDTO) {
        LivroDTO atualizado = livroService.atualizar(id, livroDTO);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Excluir livro", description = "Remove um livro do sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Livro excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
        @ApiResponse(responseCode = "400", description = "Livro possui empréstimos")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do livro") @PathVariable Long id) {
        livroService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar livros", description = "Busca livros por título, ISBN ou editora")
    public ResponseEntity<Page<LivroDTO>> buscar(
            @Parameter(description = "Termo de busca") @RequestParam String termo,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(livroService.buscarPorTermo(termo, pageable));
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Listar livros por categoria", description = "Retorna todos os livros de uma categoria")
    public ResponseEntity<List<LivroDTO>> listarPorCategoria(
            @Parameter(description = "ID da categoria") @PathVariable Long categoriaId) {
        return ResponseEntity.ok(livroService.buscarPorCategoria(categoriaId));
    }

    @GetMapping("/autor/{autorId}")
    @Operation(summary = "Listar livros por autor", description = "Retorna todos os livros de um autor")
    public ResponseEntity<List<LivroDTO>> listarPorAutor(
            @Parameter(description = "ID do autor") @PathVariable Long autorId) {
        return ResponseEntity.ok(livroService.buscarPorAutor(autorId));
    }

    @GetMapping("/disponiveis")
    @Operation(summary = "Listar livros disponíveis", description = "Retorna todos os livros disponíveis para empréstimo")
    public ResponseEntity<List<LivroDTO>> listarDisponiveis() {
        return ResponseEntity.ok(livroService.listarDisponiveis());
    }
}
