package com.biblioteca.pattern.observer;

import com.biblioteca.model.entity.Emprestimo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject que gerencia e notifica os observers de empréstimo.
 */
@Component
public class EmprestimoEventPublisher {

    private final List<EmprestimoObserver> observers;

    public EmprestimoEventPublisher(List<EmprestimoObserver> observers) {
        this.observers = new ArrayList<>(observers);
    }

    public void addObserver(EmprestimoObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(EmprestimoObserver observer) {
        observers.remove(observer);
    }

    public void notificarEmprestimoRealizado(Emprestimo emprestimo) {
        observers.forEach(observer -> observer.onEmprestimoRealizado(emprestimo));
    }

    public void notificarDevolucaoRealizada(Emprestimo emprestimo) {
        observers.forEach(observer -> observer.onDevolucaoRealizada(emprestimo));
    }

    public void notificarProximoVencimento(Emprestimo emprestimo) {
        observers.forEach(observer -> observer.onEmprestimoProximoVencimento(emprestimo));
    }

    public void notificarAtraso(Emprestimo emprestimo) {
        observers.forEach(observer -> observer.onEmprestimoAtrasado(emprestimo));
    }
}
