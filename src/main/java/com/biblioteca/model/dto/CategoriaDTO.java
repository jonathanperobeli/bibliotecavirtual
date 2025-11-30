package com.biblioteca.model.dto;

import com.biblioteca.model.entity.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO para operações de criação e atualização de categorias.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @Builder.Default
    private List<LivroDTO> livros = new ArrayList<>();

    /**
     * Converte DTO para entidade.
     */
    public Categoria toEntity() {
        return Categoria.builder()
                .id(this.id)
                .nome(this.nome)
                .descricao(this.descricao)
                .build();
    }

    /**
     * Cria DTO a partir de entidade.
     */
    public static CategoriaDTO fromEntity(Categoria categoria) {
        CategoriaDTO dto = CategoriaDTO.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .descricao(categoria.getDescricao())
                .build();
        
        if (categoria.getLivros() != null) {
            dto.setLivros(categoria.getLivros().stream()
                    .map(livro -> LivroDTO.builder()
                            .id(livro.getId())
                            .titulo(livro.getTitulo())
                            .build())
                    .toList());
        }
        
        return dto;
    }
}
