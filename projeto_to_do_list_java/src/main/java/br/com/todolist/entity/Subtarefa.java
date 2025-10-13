package br.com.todolist.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "subtarefas")
public class Subtarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private boolean status;

    public Subtarefa() {
    }

    public Subtarefa(String titulo) {
        this.titulo = titulo;
        this.status = false;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void mudarStatus() {
        this.status = !this.status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return titulo;
    }
}