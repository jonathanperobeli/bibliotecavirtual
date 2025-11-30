package com.biblioteca.model.dto;

import com.biblioteca.model.entity.Livro;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO para operações de criação e atualização de livros.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivroDTO {

    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 1, max = 200, message = "Título deve ter entre 1 e 200 caracteres")
    private String titulo;

    @Size(max = 20, message = "ISBN deve ter no máximo 20 caracteres")
    private String isbn;

    @Min(value = 1000, message = "Ano de publicação inválido")
    @Max(value = 2100, message = "Ano de publicação inválido")
    private Integer anoPublicacao;

    private String editora;

    private String edicao;

    @Positive(message = "Número de páginas deve ser positivo")
    private Integer numeroPaginas;

    private String sinopse;

    @NotNull(message = "Quantidade total é obrigatória")
    @Min(value = 1, message = "Quantidade total deve ser pelo menos 1")
    private Integer quantidadeTotal;

    private Integer quantidadeDisponivel;

    private String urlCapa;

    private LocalDate dataCadastro;

    private Livro.StatusLivro status;

    private Long categoriaId;
    private String categoriaNome;

    private List<Long> autoresIds;
    private String autoresNomes;
    
    private boolean disponivel;

    /**
     * Converte DTO para entidade.
     */
    public Livro toEntity() {
        return Livro.builder()
                .id(this.id)
                .titulo(this.titulo)
                .isbn(this.isbn)
                .anoPublicacao(this.anoPublicacao)
                .editora(this.editora)
                .edicao(this.edicao)
                .numeroPaginas(this.numeroPaginas)
                .sinopse(this.sinopse)
                .quantidadeTotal(this.quantidadeTotal)
                .quantidadeDisponivel(this.quantidadeDisponivel != null ? this.quantidadeDisponivel : this.quantidadeTotal)
                .urlCapa(this.urlCapa)
                .status(this.status != null ? this.status : Livro.StatusLivro.DISPONIVEL)
                .build();
    }

    /**
     * Cria DTO a partir de entidade.
     */
    public static LivroDTO fromEntity(Livro livro) {
        LivroDTO dto = LivroDTO.builder()
                .id(livro.getId())
                .titulo(livro.getTitulo())
                .isbn(livro.getIsbn())
                .anoPublicacao(livro.getAnoPublicacao())
                .editora(livro.getEditora())
                .edicao(livro.getEdicao())
                .numeroPaginas(livro.getNumeroPaginas())
                .sinopse(livro.getSinopse())
                .quantidadeTotal(livro.getQuantidadeTotal())
                .quantidadeDisponivel(livro.getQuantidadeDisponivel())
                .urlCapa(livro.getUrlCapa())
                .dataCadastro(livro.getDataCadastro())
                .status(livro.getStatus())
                .build();

        if (livro.getCategoria() != null) {
            dto.setCategoriaId(livro.getCategoria().getId());
            dto.setCategoriaNome(livro.getCategoria().getNome());
        }

        if (livro.getAutores() != null && !livro.getAutores().isEmpty()) {
            dto.setAutoresIds(livro.getAutores().stream()
                    .map(a -> a.getId())
                    .collect(Collectors.toList()));
            dto.setAutoresNomes(livro.getAutores().stream()
                    .map(a -> a.getNome())
                    .collect(Collectors.joining(", ")));
        }
        
        dto.setDisponivel(livro.getQuantidadeDisponivel() != null && livro.getQuantidadeDisponivel() > 0);

        return dto;
    }
}
