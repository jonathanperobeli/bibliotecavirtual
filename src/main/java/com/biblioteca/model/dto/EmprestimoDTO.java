package com.biblioteca.model.dto;

import com.biblioteca.model.entity.Emprestimo;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para operações de criação e atualização de empréstimos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmprestimoDTO {

    private Long id;

    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;
    private String usuarioNome;
    private String usuarioEmail;

    @NotNull(message = "ID do livro é obrigatório")
    private Long livroId;
    private String livroTitulo;
    private String livroIsbn;

    private LocalDate dataEmprestimo;

    private LocalDate dataPrevistaDevolucao;

    private LocalDate dataDevolucao;

    private Emprestimo.StatusEmprestimo status;

    private BigDecimal multa;

    private String observacoes;

    private Boolean atrasado;
    private Long diasAtraso;

    /**
     * Converte DTO para entidade.
     */
    public Emprestimo toEntity() {
        return Emprestimo.builder()
                .id(this.id)
                .dataEmprestimo(this.dataEmprestimo)
                .dataPrevistaDevolucao(this.dataPrevistaDevolucao)
                .dataDevolucao(this.dataDevolucao)
                .status(this.status)
                .multa(this.multa)
                .observacoes(this.observacoes)
                .build();
    }

    /**
     * Cria DTO a partir de entidade.
     */
    public static EmprestimoDTO fromEntity(Emprestimo emprestimo) {
        EmprestimoDTO dto = EmprestimoDTO.builder()
                .id(emprestimo.getId())
                .dataEmprestimo(emprestimo.getDataEmprestimo())
                .dataPrevistaDevolucao(emprestimo.getDataPrevistaDevolucao())
                .dataDevolucao(emprestimo.getDataDevolucao())
                .status(emprestimo.getStatus())
                .multa(emprestimo.getMulta())
                .observacoes(emprestimo.getObservacoes())
                .atrasado(emprestimo.isAtrasado())
                .diasAtraso(emprestimo.getDiasAtraso())
                .build();

        if (emprestimo.getUsuario() != null) {
            dto.setUsuarioId(emprestimo.getUsuario().getId());
            dto.setUsuarioNome(emprestimo.getUsuario().getNome());
            dto.setUsuarioEmail(emprestimo.getUsuario().getEmail());
        }

        if (emprestimo.getLivro() != null) {
            dto.setLivroId(emprestimo.getLivro().getId());
            dto.setLivroTitulo(emprestimo.getLivro().getTitulo());
            dto.setLivroIsbn(emprestimo.getLivro().getIsbn());
        }

        return dto;
    }
}
