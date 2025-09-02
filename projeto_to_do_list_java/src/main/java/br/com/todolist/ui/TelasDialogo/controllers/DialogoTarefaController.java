package br.com.todolist.ui.TelasDialogo.controllers;

import br.com.todolist.models.Tarefa;
import br.com.todolist.service.NotificacaoService;
import br.com.todolist.service.Orquestrador;

import java.time.LocalDate;

public class DialogoTarefaController {

    private final Orquestrador orquestrador;
    private final NotificacaoService notificacaoService;
    private final String emailUsuario;

    public DialogoTarefaController(Orquestrador orquestrador, NotificacaoService notificacaoService, String emailUsuario) {
        this.orquestrador = orquestrador;
        this.notificacaoService = notificacaoService;
        this.emailUsuario = emailUsuario;
    }

    public void salvarNovaTarefa(String titulo, String descricao, LocalDate deadline, int prioridade) {
        Tarefa novaTarefa = new Tarefa(titulo, descricao, emailUsuario, deadline, prioridade);
        orquestrador.cadastrarTarefa(novaTarefa);
        notificacaoService.notificarCriacaoTarefa(novaTarefa);
    }

    public void editarTarefa(Tarefa tarefaOriginal, String titulo, String descricao, LocalDate deadline, int prioridade) {
        orquestrador.editarTarefa(tarefaOriginal, titulo, descricao, deadline, prioridade);
        notificacaoService.notificarEdicaoTarefa(tarefaOriginal);
    }

    public void excluirTarefa(Tarefa tarefa) {
        orquestrador.excluirTarefa(tarefa);
        notificacaoService.notificarexclusaoTarefa(tarefa);
    }
}
