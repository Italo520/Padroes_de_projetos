package br.com.todolist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "eventos")
public class Evento extends Itens {

    public Evento(String titulo, String descricao, Usuario usuario, LocalDate deadline) {
        super(titulo, descricao, "Evento", usuario, deadline); // Agora passa o objeto Usuario
    }

    public Evento() {
    }

    @Override
    public String toString() {
        return getTitulo();
    }


    @Override
    public String getTitulo() {
        return super.getTitulo();
    }

    @Override
    public void setTitulo(String titulo) {
        super.setTitulo(titulo);
    }

    @Override
    public String getDescricao() {
        return super.getDescricao();
    }

    @Override
    public void setDescricao(String descricao) {
        super.setDescricao(descricao);
    }

    @Override
    public LocalDate getDataCadastro() {
        return super.getDataCadastro();
    }

    @Override
    public void setDataCadastro(LocalDate dataCadastro) {
        super.setDataCadastro(dataCadastro);
    }

    @Override
    public LocalDate getDeadline() {
        return super.getDeadline();
    }

    @Override
    public void setDeadLine(LocalDate deadline) {
        super.setDeadLine(deadline);
    }
}