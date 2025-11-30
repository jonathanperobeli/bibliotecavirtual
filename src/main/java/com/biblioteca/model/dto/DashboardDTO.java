package com.biblioteca.model.dto;

import lombok.*;

/**
 * DTO para estatísticas do dashboard.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {

    private Long totalLivros;
    private Long totalUsuarios;
    private Long totalAutores;
    private Long totalCategorias;
    private Long emprestimosAtivos;
    private Long emprestimosAtrasados;
    private Long livrosDisponiveis;
    private Long livrosIndisponiveis;
}
