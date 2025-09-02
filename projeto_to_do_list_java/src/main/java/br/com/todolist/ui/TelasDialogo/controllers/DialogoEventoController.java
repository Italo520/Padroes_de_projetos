package br.com.todolist.ui.TelasDialogo.controllers;

import br.com.todolist.models.Evento;
import br.com.todolist.service.NotificacaoService;
import br.com.todolist.service.Orquestrador;

import java.time.LocalDate;

public class DialogoEventoController {

    private final Orquestrador orquestrador;
    private final NotificacaoService notificacaoService;

    public DialogoEventoController(Orquestrador orquestrador, NotificacaoService notificacaoService) {
        this.orquestrador = orquestrador;
        this.notificacaoService = notificacaoService;
    }

    public boolean salvarNovoEvento(String titulo, String descricao, LocalDate deadline) {
        boolean sucesso = orquestrador.cadastrarEvento(titulo, descricao, deadline);
        if (sucesso) {
            // A notificação só deve ocorrer se o evento for salvo com sucesso.
            // A lógica de criação do evento já não está mais aqui.
            // Apenas para demonstração, vamos criar um evento "falso" para notificar.
            // O ideal seria o método de cadastro retornar o evento criado.
            Evento evento = new Evento(titulo, descricao, "", deadline); // Email do usuário não está disponível aqui
            notificacaoService.notificarCriacaoEvento(evento);
        }
        return sucesso;
    }

    public void editarEvento(Evento eventoOriginal, String titulo, String descricao, LocalDate deadline) {
        orquestrador.editarEvento(eventoOriginal, titulo, descricao, deadline);
        notificacaoService.notificarEdicaoEvento(eventoOriginal);
    }

    public void excluirEvento(Evento evento) {
        orquestrador.excluirEvento(evento);
        notificacaoService.notificarExclusaoEvento(evento);
    }
}
