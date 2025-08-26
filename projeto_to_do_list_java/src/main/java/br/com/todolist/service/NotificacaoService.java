package br.com.todolist.service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import br.com.todolist.models.Evento;
import br.com.todolist.models.Tarefa;
import br.com.todolist.util.Mensageiro;

public class NotificacaoService {

    private Mensageiro mensageiro;
    private String emailUsuario;

    public NotificacaoService(String emailUsuario) {
        this.emailUsuario = emailUsuario;
        this.mensageiro = new Mensageiro();
    }


// ---------------Notificações de Tarefas------------------

    public void notificarCriacaoTarefa(Tarefa tarefa) {
        String assunto = "Notificação: Nova Tarefa Criada";
        String corpo = "Uma nova tarefa foi criada:\n\n"
                + "Título: " + tarefa.getTitulo() + "\n"
                + "Descrição: " + tarefa.getDescricao() + "\n"
                + "Prazo: " + tarefa.getDeadline() + "\n";
        mensageiro.enviarEmail(this.emailUsuario, assunto, corpo);
    }

    public void notificarexclusaoTarefa(Tarefa tarefa) {
        String assunto = "Notificação: Tarefa Excluída";
        String corpo = "A tarefa '" + tarefa.getTitulo() + "' foi excluída.";
        mensageiro.enviarEmail(this.emailUsuario, assunto, corpo);
    }

    public void notificarEdicaoTarefa(Tarefa tarefa) {
        String assunto = "Notificação: Tarefa Editada";
        String corpo = "A tarefa '" + tarefa.getTitulo() + "' foi editada.";
        mensageiro.enviarEmail(this.emailUsuario, assunto, corpo);
    }

    public boolean enviarRelatorioTarefasDoDiaPorEmail(LocalDate dia, String nomeArquivo) {
        String assunto = "Seu Relatório de Tarefas do Dia: " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String corpo = "Olá!\n\nSegue em anexo o relatório com suas tarefas para o dia " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + ".\n\nAtenciosamente,\nSistema ToDoList.";
        boolean sucesso = mensageiro.enviarEmailComAnexo(this.emailUsuario, assunto, corpo, nomeArquivo);
        if (sucesso) {
            new File(nomeArquivo).delete();
            return true;
        }
        return false;
    }



// ---------------Notificações de Eventos------------------

    public void notificarCriacaoEvento(Evento evento) {
        String assunto = "Notificação: Novo Evento Criado";
        String corpo = "Um novo evento foi criado:\n\n"
                + "Título: " + evento.getTitulo() + "\n"
                + "Descrição: " + evento.getDescricao() + "\n"
                + "Prazo: " + evento.getDeadline() + "\n";
        mensageiro.enviarEmail(this.emailUsuario, assunto, corpo);
    }

    public void notificarExclusaoEvento(Evento evento) {
        String assunto = "Notificação: Evento Excluído";
        String corpo = "O evento '" + evento.getTitulo() + "' foi excluído.";
        mensageiro.enviarEmail(this.emailUsuario, assunto, corpo);
    }

    public void notificarEdicaoEvento(Evento evento) {
        String assunto = "Notificação: Evento Editado";
        String corpo = "O evento '" + evento.getTitulo() + "' foi editado.";
        mensageiro.enviarEmail(this.emailUsuario, assunto, corpo);
    }


    
}
