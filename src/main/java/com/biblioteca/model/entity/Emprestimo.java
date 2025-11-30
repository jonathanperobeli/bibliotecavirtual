package com.biblioteca.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Entidade que representa um Empréstimo de livro.
 * 
 * Relacionamentos:
 * - Many-to-One com Usuario
 * - Many-to-One com Livro
 * 
 * Implementa lógica de cálculo de multa por atraso.
 */
@Entity
@Table(name = "emprestimos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Emprestimo {

    private static final int DIAS_EMPRESTIMO_PADRAO = 14;
    private static final BigDecimal VALOR_MULTA_POR_DIA = new BigDecimal("2.00");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @Column(name = "data_prevista_devolucao", nullable = false)
    private LocalDate dataPrevistaDevolucao;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEmprestimo status;

    @Column(precision = 10, scale = 2)
    private BigDecimal multa;

    @Column(length = 500)
    private String observacoes;

    @PrePersist
    protected void onCreate() {
        if (dataEmprestimo == null) {
            dataEmprestimo = LocalDate.now();
        }
        if (dataPrevistaDevolucao == null) {
            dataPrevistaDevolucao = dataEmprestimo.plusDays(DIAS_EMPRESTIMO_PADRAO);
        }
        if (status == null) {
            status = StatusEmprestimo.ATIVO;
        }
    }

    /**
     * Verifica se o empréstimo está em atraso.
     */
    public boolean isAtrasado() {
        if (status == StatusEmprestimo.DEVOLVIDO) {
            return dataDevolucao.isAfter(dataPrevistaDevolucao);
        }
        return LocalDate.now().isAfter(dataPrevistaDevolucao);
    }

    /**
     * Calcula os dias de atraso.
     */
    public long getDiasAtraso() {
        if (!isAtrasado()) {
            return 0;
        }
        LocalDate dataReferencia = (dataDevolucao != null) ? dataDevolucao : LocalDate.now();
        return ChronoUnit.DAYS.between(dataPrevistaDevolucao, dataReferencia);
    }

    /**
     * Calcula a multa por atraso.
     */
    public BigDecimal calcularMulta() {
        long diasAtraso = getDiasAtraso();
        if (diasAtraso > 0) {
            return VALOR_MULTA_POR_DIA.multiply(BigDecimal.valueOf(diasAtraso));
        }
        return BigDecimal.ZERO;
    }

    /**
     * Realiza a devolução do empréstimo.
     */
    public void realizarDevolucao() {
        this.dataDevolucao = LocalDate.now();
        this.multa = calcularMulta();
        this.status = StatusEmprestimo.DEVOLVIDO;
        this.livro.devolver();
    }

    /**
     * Renova o empréstimo por mais um período.
     */
    public void renovar() {
        if (status == StatusEmprestimo.ATIVO && !isAtrasado()) {
            this.dataPrevistaDevolucao = this.dataPrevistaDevolucao.plusDays(DIAS_EMPRESTIMO_PADRAO);
            this.status = StatusEmprestimo.RENOVADO;
        }
    }

    /**
     * Enum que define os status possíveis de um empréstimo.
     */
    public enum StatusEmprestimo {
        ATIVO,      // Empréstimo em andamento
        DEVOLVIDO,  // Livro devolvido
        ATRASADO,   // Prazo excedido
        RENOVADO,   // Empréstimo renovado
        CANCELADO   // Empréstimo cancelado
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Emprestimo that = (Emprestimo) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
