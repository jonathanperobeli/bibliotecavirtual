package com.biblioteca.pattern.factory;

import com.biblioteca.model.entity.Emprestimo;
import com.biblioteca.model.entity.Livro;
import com.biblioteca.model.entity.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Factory concreta para empréstimos padrão (14 dias).
 */
@Component
public class EmprestimoPadraoFactory extends EmprestimoFactory {

    private static final int DIAS_EMPRESTIMO_PADRAO = 14;

    @Override
    public Emprestimo criarEmprestimo(Usuario usuario, Livro livro) {
        validarPreCondicoes(usuario, livro);

        LocalDate dataEmprestimo = LocalDate.now();
        LocalDate dataPrevistaDevolucao = dataEmprestimo.plusDays(DIAS_EMPRESTIMO_PADRAO);

        return Emprestimo.builder()
                .usuario(usuario)
                .livro(livro)
                .dataEmprestimo(dataEmprestimo)
                .dataPrevistaDevolucao(dataPrevistaDevolucao)
                .status(Emprestimo.StatusEmprestimo.ATIVO)
                .build();
    }
}
