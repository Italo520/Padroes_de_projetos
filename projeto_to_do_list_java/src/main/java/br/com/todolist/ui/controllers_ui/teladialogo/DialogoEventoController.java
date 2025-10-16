// Em: src/main/java/br/com/todolist/ui/controllers/teladialogo/DialogoEventoController.java
package br.com.todolist.ui.controllers_ui.teladialogo;

import br.com.todolist.entity.Evento;
import br.com.todolist.service.NotificacaoService;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.ui.views_ui.teladialogo.DialogoEventoView;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import br.com.todolist.entity.Usuario;

public class DialogoEventoController {

    private final DialogoEventoView view;
    private final Orquestrador orquestrador;
    private final NotificacaoService notificacaoService;
    private final Evento eventoOriginal;
    private final Usuario usuario;
    private final DateTimeFormatter formatadorDeData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DialogoEventoController(DialogoEventoView view, Orquestrador orquestrador, NotificacaoService notificacaoService, Evento evento, Usuario usuario) {
        this.view = view;
        this.orquestrador = orquestrador;
        this.notificacaoService = notificacaoService;
        this.eventoOriginal = evento;
        this.usuario = usuario;
        this.view.setController(this);
    }

    public void salvar() {
        String titulo = view.getTitulo();
        if (titulo.isEmpty()) {
            view.exibirMensagem("Erro de Validação", "O campo 'Título' é obrigatório.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate deadline = LocalDate.parse(view.getData(), formatadorDeData);
            String descricao = view.getDescricao();

            boolean sucesso;
            if (eventoOriginal == null) {
                sucesso = salvarNovoEvento(titulo, descricao, deadline);
            } else {
                editarEvento(titulo, descricao, deadline);
                sucesso = true;
            }

            if (sucesso) {
                view.marcarComoSalvo();
            }

        } catch (DateTimeParseException e) {
            view.exibirMensagem("Erro de Formato", "O formato da data é inválido. Use dd/MM/yyyy.", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean salvarNovoEvento(String titulo, String descricao, LocalDate deadline) {
        Evento novoEvento = new Evento(titulo, descricao, usuario, deadline);
        boolean sucesso = orquestrador.cadastrarEvento(novoEvento);
        if (sucesso) {
            notificacaoService.notificarCriacaoEvento(novoEvento);
            return true;
        } else {
            view.exibirMensagem("Erro ao Cadastrar", "Não foi possível cadastrar o evento.\nVerifique se já não existe um evento com a mesma data.", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    private void editarEvento(String titulo, String descricao, LocalDate deadline) {
        orquestrador.editarEvento(eventoOriginal, titulo, descricao, deadline);
        notificacaoService.notificarEdicaoEvento(eventoOriginal);
    }
}