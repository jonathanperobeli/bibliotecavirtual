package com.biblioteca.model.dto;

import com.biblioteca.model.entity.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para operações de criação e atualização de autores.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutorDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 150, message = "Nome deve ter entre 2 e 150 caracteres")
    private String nome;

    private String nacionalidade;

    private LocalDate dataNascimento;

    private String biografia;

    @Builder.Default
    private List<LivroDTO> livros = new ArrayList<>();

    /**
     * Converte DTO para entidade.
     */
    public Autor toEntity() {
        return Autor.builder()
                .id(this.id)
                .nome(this.nome)
                .nacionalidade(this.nacionalidade)
                .dataNascimento(this.dataNascimento)
                .biografia(this.biografia)
                .build();
    }

    /**
     * Cria DTO a partir de entidade.
     */
    public static AutorDTO fromEntity(Autor autor) {
        AutorDTO dto = AutorDTO.builder()
                .id(autor.getId())
                .nome(autor.getNome())
                .nacionalidade(autor.getNacionalidade())
                .dataNascimento(autor.getDataNascimento())
                .biografia(autor.getBiografia())
                .build();
        
        if (autor.getLivros() != null) {
            dto.setLivros(autor.getLivros().stream()
                    .map(livro -> LivroDTO.builder()
                            .id(livro.getId())
                            .titulo(livro.getTitulo())
                            .build())
                    .toList());
        }
        
        return dto;
    }
}
