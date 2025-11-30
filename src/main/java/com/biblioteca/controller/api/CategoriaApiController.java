package com.biblioteca.controller.api;

import com.biblioteca.model.dto.CategoriaDTO;
import com.biblioteca.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações com Categorias.
 */
@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorias", description = "API para gerenciamento de categorias")
public class CategoriaApiController {

    private final CategoriaService categoriaService;

    public CategoriaApiController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as categorias", description = "Retorna uma lista com todas as categorias cadastradas")
    @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso")
    public ResponseEntity<List<CategoriaDTO>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID", description = "Retorna uma categoria específica pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<CategoriaDTO> buscarPorId(
            @Parameter(description = "ID da categoria") @PathVariable Long id) {
        return categoriaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar nova categoria", description = "Cadastra uma nova categoria no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou categoria já existe")
    })
    public ResponseEntity<CategoriaDTO> criar(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        CategoriaDTO criada = categoriaService.salvar(categoriaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar categoria", description = "Atualiza os dados de uma categoria existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<CategoriaDTO> atualizar(
            @Parameter(description = "ID da categoria") @PathVariable Long id,
            @Valid @RequestBody CategoriaDTO categoriaDTO) {
        CategoriaDTO atualizada = categoriaService.atualizar(id, categoriaDTO);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Excluir categoria", description = "Remove uma categoria do sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
        @ApiResponse(responseCode = "400", description = "Categoria possui livros associados")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da categoria") @PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar categorias por nome", description = "Busca categorias que contenham o termo no nome")
    public ResponseEntity<List<CategoriaDTO>> buscarPorNome(
            @Parameter(description = "Nome da categoria") @RequestParam String nome) {
        return ResponseEntity.ok(categoriaService.buscarPorNome(nome));
    }
}
