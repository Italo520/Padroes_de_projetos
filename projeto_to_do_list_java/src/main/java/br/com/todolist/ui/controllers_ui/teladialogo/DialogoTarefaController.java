// Em: src/main/java/br/com/todolist/ui/controllers/teladialogo/DialogoTarefaController.java
package br.com.todolist.ui.controllers_ui.teladialogo;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.service.NotificacaoService;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.ui.views_ui.teladialogo.DialogoTarefaView;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import br.com.todolist.entity.Usuario;

public class DialogoTarefaController {

    private final DialogoTarefaView view;
    private final Orquestrador orquestrador;
    private final NotificacaoService notificacaoService;
    private final Usuario usuario;
    private final Tarefa tarefaOriginal;
    private final DateTimeFormatter formatadorDeData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DialogoTarefaController(DialogoTarefaView view, Orquestrador orquestrador, NotificacaoService notificacaoService, Usuario usuario, Tarefa tarefa) {
        this.view = view;
        this.orquestrador = orquestrador;
        this.notificacaoService = notificacaoService;
        this.usuario = usuario;
        this.tarefaOriginal = tarefa;
        this.view.setController(this);
    }

    public void salvar() {
        String titulo = view.getTitulo();
        if (titulo.isEmpty()) {
            view.exibirMensagem("Erro de Validação", "O campo 'Título' é obrigatório.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate prazo = LocalDate.parse(view.getPrazo(), formatadorDeData);
            String descricao = view.getDescricao();
            int prioridade = view.getPrioridade();

            if (tarefaOriginal == null) { // Criando uma nova tarefa
                salvarNovaTarefa(titulo, descricao, prazo, prioridade);
            } else { // Editando uma tarefa existente
                editarTarefa(titulo, descricao, prazo, prioridade);
            }
            view.marcarComoSalvo();

        } catch (DateTimeParseException e) {
            view.exibirMensagem("Erro de Formato", "Formato de data inválido. Use dd/MM/yyyy.", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarNovaTarefa(String titulo, String descricao, LocalDate deadline, int prioridade) {
        Tarefa novaTarefa = new Tarefa(titulo, descricao, usuario, deadline, prioridade);
        orquestrador.cadastrarTarefa(novaTarefa);
        notificacaoService.notificarCriacaoTarefa(novaTarefa);
    }

    private void editarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade) {
        orquestrador.editarTarefa(tarefaOriginal, titulo, descricao, deadline, prioridade);
        notificacaoService.notificarEdicaoTarefa(tarefaOriginal);
    }
}