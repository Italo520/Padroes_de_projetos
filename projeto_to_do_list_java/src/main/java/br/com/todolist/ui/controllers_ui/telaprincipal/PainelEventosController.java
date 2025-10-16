package br.com.todolist.ui.controllers_ui.telaprincipal;

import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Usuario;
import br.com.todolist.service.NotificacaoService;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.ui.controllers_ui.teladialogo.DialogoEventoController;
import br.com.todolist.ui.views_ui.teladialogo.DialogoEventoView;
import br.com.todolist.ui.views_ui.telaprincipal.PainelEventosView;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.List;

public class PainelEventosController {
    private final PainelEventosView view;
    private final Orquestrador orquestrador;
    private final NotificacaoService notificacaoService;
    private final Usuario usuario;

    public PainelEventosController(PainelEventosView view, Orquestrador orquestrador, NotificacaoService notificacaoService, Usuario usuario) {
        this.view = view;
        this.orquestrador = orquestrador;
        this.notificacaoService = notificacaoService;
        this.usuario = usuario;
        this.view.setController(this);
        atualizarListaPrincipal();
    }

    public void atualizarListaPrincipal() {
        List<Evento> eventos = orquestrador.listarTodosEventos();
        view.popularListaEventos(eventos);
    }

    public void novoEvento() {
        DialogoEventoView dialogoView = new DialogoEventoView((JFrame) SwingUtilities.getWindowAncestor(view), "Novo Evento", null);
        new DialogoEventoController(dialogoView, orquestrador, notificacaoService, null, usuario);
        dialogoView.setVisible(true);

        if (dialogoView.foiSalvo()) {
            atualizarListaPrincipal();
        }
    }

    public void editarEvento() {
        Evento eventoSelecionado = view.getEventoSelecionado();
        if (eventoSelecionado == null) {
            JOptionPane.showMessageDialog(view, "Selecione um evento para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DialogoEventoView dialogoView = new DialogoEventoView((JFrame) SwingUtilities.getWindowAncestor(view), "Editar Evento", eventoSelecionado);
        new DialogoEventoController(dialogoView, orquestrador, notificacaoService, eventoSelecionado, usuario);
        dialogoView.setVisible(true);

        if (dialogoView.foiSalvo()) {
            atualizarListaPrincipal();
        }
    }

    public void excluirEvento() {
        Evento eventoSelecionado = view.getEventoSelecionado();
        if (eventoSelecionado == null) {
            JOptionPane.showMessageDialog(view, "Selecione um evento para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(view, "Tem certeza que deseja excluir o evento '" + eventoSelecionado.getTitulo() + "'?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            orquestrador.excluirEvento(eventoSelecionado);
            notificacaoService.notificarExclusaoEvento(eventoSelecionado);
            atualizarListaPrincipal();
        }
    }

    public void selecionarEvento() {
        Evento eventoSelecionado = view.getEventoSelecionado();
        view.atualizarDetalhesEvento(eventoSelecionado);
    }
}