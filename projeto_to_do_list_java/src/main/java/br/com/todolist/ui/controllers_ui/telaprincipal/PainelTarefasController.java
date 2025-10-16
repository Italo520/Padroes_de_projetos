package br.com.todolist.ui.controllers_ui.telaprincipal;

import br.com.todolist.entity.Subtarefa;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.service.NotificacaoService;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.ui.controllers_ui.teladialogo.DialogoTarefaController;
import br.com.todolist.ui.views_ui.teladialogo.DialogoTarefaView;
import br.com.todolist.ui.views_ui.telaprincipal.PainelTarefasView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import br.com.todolist.entity.Usuario;

public class PainelTarefasController extends MouseAdapter {
    private final PainelTarefasView view;
    private final Orquestrador orquestrador;
    private final NotificacaoService notificacaoService;
    private final Usuario usuario;

    public PainelTarefasController(PainelTarefasView view, Orquestrador orquestrador, NotificacaoService notificacaoService, Usuario usuario) {
        this.view = view;
        this.orquestrador = orquestrador;
        this.notificacaoService = notificacaoService;
        this.usuario = usuario;
        this.view.setController(this);
        atualizarListaPrincipal();
    }

    public void atualizarListaPrincipal() {
        List<Tarefa> tarefas = orquestrador.listarTodasTarefas();
        view.popularListaTarefas(tarefas);
    }

    public void novaTarefa() {
        DialogoTarefaView dialogoView = new DialogoTarefaView((JFrame) SwingUtilities.getWindowAncestor(view), "Nova Tarefa", null);
        new DialogoTarefaController(dialogoView, orquestrador, notificacaoService, usuario, null);
        dialogoView.setVisible(true);
        if (dialogoView.foiSalvo()) {
            atualizarListaPrincipal();
        }
    }

    public void editarTarefa() {
        Tarefa tarefaSelecionada = view.getTarefaSelecionada();
        if (tarefaSelecionada == null) {
            JOptionPane.showMessageDialog(view, "Por favor, selecione uma tarefa para editar.", "Nenhuma Tarefa Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DialogoTarefaView dialogoView = new DialogoTarefaView((JFrame) SwingUtilities.getWindowAncestor(view), "Editar Tarefa", tarefaSelecionada);
        new DialogoTarefaController(dialogoView, orquestrador, notificacaoService, usuario, tarefaSelecionada);
        dialogoView.setVisible(true);
        if (dialogoView.foiSalvo()) {
            atualizarListaPrincipal();
        }
    }

    public void excluirTarefa() {
        Tarefa tarefaSelecionada = view.getTarefaSelecionada();
        if (tarefaSelecionada == null) {
            JOptionPane.showMessageDialog(view, "Por favor, selecione uma tarefa para excluir.", "Nenhuma Tarefa Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(view, "Tem certeza que deseja excluir a tarefa:\n" + tarefaSelecionada.getTitulo(), "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resposta == JOptionPane.YES_OPTION) {
            orquestrador.excluirTarefa(tarefaSelecionada);
            notificacaoService.notificarexclusaoTarefa(tarefaSelecionada);
            atualizarListaPrincipal();
        }
    }

    public void selecionarTarefa() {
        Tarefa tarefaSelecionada = view.getTarefaSelecionada();
        view.atualizarListaSubtarefas(tarefaSelecionada);
        view.atualizarDetalhesTarefa(tarefaSelecionada);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JList<Subtarefa> listaDeSubtarefas = view.getListaDeSubtarefas();
        int index = listaDeSubtarefas.locationToIndex(e.getPoint());
        if (index != -1) {
            Tarefa tarefaPai = view.getTarefaSelecionada();
            Subtarefa subtarefa = listaDeSubtarefas.getModel().getElementAt(index);
            subtarefa.mudarStatus();
            if (tarefaPai != null) {
                orquestrador.atualizarTarefa(tarefaPai);
                view.atualizarDetalhesTarefa(tarefaPai);
            }
            listaDeSubtarefas.repaint(listaDeSubtarefas.getCellBounds(index, index));
        }
    }

    public void novaSubtarefa() {
        Tarefa tarefaPai = view.getTarefaSelecionada();
        if (tarefaPai == null) {
            JOptionPane.showMessageDialog(view, "Selecione uma tarefa principal para adicionar uma subtarefa.", "Nenhuma Tarefa Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String descricao = JOptionPane.showInputDialog(view, "Descrição da nova subtarefa:", "Nova Subtarefa", JOptionPane.PLAIN_MESSAGE);
        if (descricao != null && !descricao.trim().isEmpty()) {
            tarefaPai.adicionarSubtarefa(new Subtarefa(descricao));
            orquestrador.atualizarTarefa(tarefaPai);
            view.atualizarListaSubtarefas(tarefaPai);
            view.atualizarDetalhesTarefa(tarefaPai);
        }
    }

    public void editarSubtarefa() {
        Tarefa tarefaPai = view.getTarefaSelecionada();
        Subtarefa subtarefa = view.getSubtarefaSelecionada();
        if (tarefaPai == null || subtarefa == null) {
            JOptionPane.showMessageDialog(view, "Selecione uma subtarefa para editar.", "Nenhuma Subtarefa Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String novoTitulo = (String) JOptionPane.showInputDialog(view, "Nova descrição:", "Editar Subtarefa", JOptionPane.PLAIN_MESSAGE, null, null, subtarefa.getTitulo());
        if (novoTitulo != null && !novoTitulo.trim().isEmpty()) {
            subtarefa.setTitulo(novoTitulo);
            orquestrador.atualizarTarefa(tarefaPai);
            view.atualizarListaSubtarefas(tarefaPai);
        }
    }

    public void excluirSubtarefa() {
        Tarefa tarefaPai = view.getTarefaSelecionada();
        Subtarefa subtarefa = view.getSubtarefaSelecionada();
        if (tarefaPai == null || subtarefa == null) {
            JOptionPane.showMessageDialog(view, "Selecione uma subtarefa para excluir.", "Nenhuma Subtarefa Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(view, "Excluir a subtarefa '" + subtarefa.getTitulo() + "'?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            tarefaPai.removerSubtarefa(subtarefa);
            orquestrador.atualizarTarefa(tarefaPai);
            view.atualizarListaSubtarefas(tarefaPai);
            view.atualizarDetalhesTarefa(tarefaPai);
        }
    }
}