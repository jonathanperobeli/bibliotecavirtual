package com.biblioteca.pattern.factory;

import com.biblioteca.model.entity.Emprestimo;
import com.biblioteca.model.entity.Livro;
import com.biblioteca.model.entity.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Factory concreta para empréstimos estendidos (30 dias).
 * Usado para professores e funcionários.
 */
@Component
public class EmprestimoEstendidoFactory extends EmprestimoFactory {

    private static final int DIAS_EMPRESTIMO_ESTENDIDO = 30;

    @Override
    public Emprestimo criarEmprestimo(Usuario usuario, Livro livro) {
        validarPreCondicoes(usuario, livro);

        LocalDate dataEmprestimo = LocalDate.now();
        LocalDate dataPrevistaDevolucao = dataEmprestimo.plusDays(DIAS_EMPRESTIMO_ESTENDIDO);

        return Emprestimo.builder()
                .usuario(usuario)
                .livro(livro)
                .dataEmprestimo(dataEmprestimo)
                .dataPrevistaDevolucao(dataPrevistaDevolucao)
                .status(Emprestimo.StatusEmprestimo.ATIVO)
                .observacoes("Empréstimo estendido - " + DIAS_EMPRESTIMO_ESTENDIDO + " dias")
                .build();
    }
}
