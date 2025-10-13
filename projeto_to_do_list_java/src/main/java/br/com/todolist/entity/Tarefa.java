package br.com.todolist.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tarefas")
public class Tarefa extends Itens {

    private LocalDate dataConclusao;
    private int prioridade;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "tarefa_id")
    private List<Subtarefa> subtarefas;

    public Tarefa() {
    }

    public Tarefa(String titulo, String descricao, Usuario usuario, LocalDate deadline, int prioridade) {
        super(titulo, descricao, "Tarefa", usuario, deadline);
        this.prioridade = prioridade;
        this.dataConclusao = null;
        this.subtarefas = new ArrayList<>();
    }

    public double obterPercentual() {
        if (subtarefas == null || subtarefas.isEmpty()) {
            return dataConclusao != null ? 100.0 : 0.0;
        }

        long subtarefasConcluidas = this.subtarefas.stream()
                .filter(Subtarefa::isStatus)
                .count();

        return ((double) subtarefasConcluidas / subtarefas.size()) * 100;
    }

    // Getters e Setters

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public List<Subtarefa> getSubtarefas() {
        return subtarefas;
    }

    public void adicionarSubtarefa(Subtarefa subtarefa) {
        if (this.subtarefas == null) {
            this.subtarefas = new ArrayList<>();
        }
        this.subtarefas.add(subtarefa);
    }

    public void removerSubtarefa(Subtarefa subtarefa) {
        if (this.subtarefas != null) {
            this.subtarefas.remove(subtarefa);
        }
    }

    @Override
    public String toString() {
        return getTitulo();
    }
}