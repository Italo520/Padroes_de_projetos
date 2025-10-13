package br.com.todolist.ui.views_ui.telaprincipal;

import br.com.todolist.entity.Subtarefa;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.ui.core.PainelBase;
import br.com.todolist.ui.core.SubtarefaCellRenderer;
import br.com.todolist.ui.controllers.telaprincipal.PainelTarefasController;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PainelTarefasView extends PainelBase {

    private final DateTimeFormatter formatadorDeData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DefaultListModel<Tarefa> modeloListaTarefas;
    private DefaultListModel<Subtarefa> modeloListaSubtarefas;
    private JList<Tarefa> listaDeTarefas;
    private JList<Subtarefa> listaDeSubtarefas;

    private JLabel valorDescricao;
    private JLabel valorPrioridade;
    private JLabel valorPrazo;
    private JLabel valorConclusao;

    // Botões que o controller precisa acessar
    private JButton botaoNovaTarefa;
    private JButton botaoEditarTarefa;
    private JButton botaoExcluirTarefa;
    private JButton botaoNovaSubtarefa;
    private JButton botaoEditarSubtarefa;
    private JButton botaoExcluirSubtarefa;

    public PainelTarefasView() {
        super();
        super.inicializarLayout();
    }

    @Override
    protected JPanel criarPainelDeBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        botaoNovaTarefa = new JButton("Nova Tarefa");
        botaoEditarTarefa = new JButton("Editar Tarefa");
        botaoExcluirTarefa = new JButton("Excluir Tarefa");

        painel.add(botaoNovaTarefa);
        painel.add(botaoEditarTarefa);
        painel.add(botaoExcluirTarefa);

        return painel;
    }

    @Override
    protected JPanel criarPainelDeConteudo() {
        modeloListaTarefas = new DefaultListModel<>();
        listaDeTarefas = new JList<>(modeloListaTarefas);
        listaDeTarefas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTarefas = new JScrollPane(listaDeTarefas);
        scrollTarefas.setBorder(BorderFactory.createTitledBorder("Tarefas"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTarefas, criarPainelDireito());
        splitPane.setResizeWeight(0.5);

        JPanel painelConteudo = new JPanel(new BorderLayout());
        painelConteudo.add(splitPane, BorderLayout.CENTER);
        return painelConteudo;
    }

    private JPanel criarPainelDireito() {
        JPanel painelDireito = new JPanel(new BorderLayout(5, 5));
        painelDireito.setBorder(BorderFactory.createTitledBorder("Subtarefas e Detalhes"));
        painelDireito.add(criarPainelDetalhes(), BorderLayout.NORTH);
        painelDireito.add(criarPainelSubtarefas(), BorderLayout.CENTER);
        return painelDireito;
    }

    private JPanel criarPainelDetalhes() {
        JPanel painelDetalhes = new JPanel(new GridLayout(0, 2, 5, 5));
        painelDetalhes.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        valorDescricao = new JLabel("N/D");
        valorPrioridade = new JLabel("N/D");
        valorPrazo = new JLabel("N/D");
        valorConclusao = new JLabel("N/D");
        painelDetalhes.add(new JLabel("Descrição:"));
        painelDetalhes.add(valorDescricao);
        painelDetalhes.add(new JLabel("Prioridade:"));
        painelDetalhes.add(valorPrioridade);
        painelDetalhes.add(new JLabel("Prazo:"));
        painelDetalhes.add(valorPrazo);
        painelDetalhes.add(new JLabel("Conclusão:"));
        painelDetalhes.add(valorConclusao);
        return painelDetalhes;
    }

    private JPanel criarPainelSubtarefas() {
        JPanel painelSubtarefas = new JPanel(new BorderLayout());
        modeloListaSubtarefas = new DefaultListModel<>();
        listaDeSubtarefas = new JList<>(modeloListaSubtarefas);
        listaDeSubtarefas.setCellRenderer(new SubtarefaCellRenderer());

        JPanel painelBotoesSubtarefa = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoNovaSubtarefa = new JButton("Nova Subtarefa");
        botaoEditarSubtarefa = new JButton("Editar Subtarefa");
        botaoExcluirSubtarefa = new JButton("Excluir Subtarefa");
        painelBotoesSubtarefa.add(botaoNovaSubtarefa);
        painelBotoesSubtarefa.add(botaoEditarSubtarefa);
        painelBotoesSubtarefa.add(botaoExcluirSubtarefa);

        painelSubtarefas.add(new JScrollPane(listaDeSubtarefas), BorderLayout.CENTER);
        painelSubtarefas.add(painelBotoesSubtarefa, BorderLayout.SOUTH);
        return painelSubtarefas;
    }

    // Métodos para o Controller
    public void setController(PainelTarefasController controller) {
        botaoNovaTarefa.addActionListener(e -> controller.novaTarefa());
        botaoEditarTarefa.addActionListener(e -> controller.editarTarefa());
        botaoExcluirTarefa.addActionListener(e -> controller.excluirTarefa());
        botaoNovaSubtarefa.addActionListener(e -> controller.novaSubtarefa());
        botaoEditarSubtarefa.addActionListener(e -> controller.editarSubtarefa());
        botaoExcluirSubtarefa.addActionListener(e -> controller.excluirSubtarefa());
        listaDeTarefas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                controller.selecionarTarefa();
            }
        });
        listaDeSubtarefas.addMouseListener(controller);
    }

    public void atualizarDetalhesTarefa(Tarefa tarefa) {
        if (tarefa != null) {
            valorDescricao.setText(tarefa.getDescricao());
            valorPrioridade.setText(String.valueOf(tarefa.getPrioridade()));
            valorPrazo.setText(tarefa.getDeadline().format(formatadorDeData));
            valorConclusao.setText((int) tarefa.obterPercentual() + "% ");
        } else {
            valorDescricao.setText("Selecione uma tarefa");
            valorPrioridade.setText("-");
            valorPrazo.setText("-");
            valorConclusao.setText("-");
        }
    }

    public void popularListaTarefas(List<Tarefa> tarefas) {
        modeloListaTarefas.clear();
        if (tarefas != null) {
            tarefas.forEach(modeloListaTarefas::addElement);
        }
        atualizarListaSubtarefas(null);
        atualizarDetalhesTarefa(null);
    }

    public void atualizarListaSubtarefas(Tarefa tarefa) {
        modeloListaSubtarefas.clear();
        if (tarefa != null && tarefa.getSubtarefas() != null) {
            tarefa.getSubtarefas().forEach(modeloListaSubtarefas::addElement);
        }
        listaDeTarefas.repaint();
    }

    public Tarefa getTarefaSelecionada() {
        return listaDeTarefas.getSelectedValue();
    }

    public Subtarefa getSubtarefaSelecionada() {
        return listaDeSubtarefas.getSelectedValue();
    }

    public JList<Subtarefa> getListaDeSubtarefas(){
        return this.listaDeSubtarefas;
    }
}