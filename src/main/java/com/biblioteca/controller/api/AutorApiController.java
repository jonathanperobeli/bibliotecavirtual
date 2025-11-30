package com.biblioteca.controller.api;

import com.biblioteca.model.dto.AutorDTO;
import com.biblioteca.service.AutorService;
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
 * Controller REST para operações com Autores.
 */
@RestController
@RequestMapping("/api/autores")
@Tag(name = "Autores", description = "API para gerenciamento de autores")
public class AutorApiController {

    private final AutorService autorService;

    public AutorApiController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os autores", description = "Retorna uma lista com todos os autores cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de autores retornada com sucesso")
    public ResponseEntity<List<AutorDTO>> listarTodos() {
        return ResponseEntity.ok(autorService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar autor por ID", description = "Retorna um autor específico pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Autor encontrado"),
        @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    })
    public ResponseEntity<AutorDTO> buscarPorId(
            @Parameter(description = "ID do autor") @PathVariable Long id) {
        return autorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar novo autor", description = "Cadastra um novo autor no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Autor criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<AutorDTO> criar(@Valid @RequestBody AutorDTO autorDTO) {
        AutorDTO criado = autorService.salvar(autorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar autor", description = "Atualiza os dados de um autor existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Autor atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    })
    public ResponseEntity<AutorDTO> atualizar(
            @Parameter(description = "ID do autor") @PathVariable Long id,
            @Valid @RequestBody AutorDTO autorDTO) {
        AutorDTO atualizado = autorService.atualizar(id, autorDTO);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Excluir autor", description = "Remove um autor do sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Autor excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
        @ApiResponse(responseCode = "400", description = "Autor possui livros associados")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do autor") @PathVariable Long id) {
        autorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar autores por nome", description = "Busca autores que contenham o termo no nome")
    public ResponseEntity<List<AutorDTO>> buscarPorNome(
            @Parameter(description = "Nome do autor") @RequestParam String nome) {
        return ResponseEntity.ok(autorService.buscarPorNome(nome));
    }

    @GetMapping("/nacionalidades")
    @Operation(summary = "Listar nacionalidades", description = "Retorna lista de nacionalidades dos autores")
    public ResponseEntity<List<String>> listarNacionalidades() {
        return ResponseEntity.ok(autorService.listarNacionalidades());
    }
}
