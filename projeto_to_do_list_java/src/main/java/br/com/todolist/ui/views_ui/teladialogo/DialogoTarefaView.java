
package br.com.todolist.ui.views_ui.teladialogo;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.ui.controllers_ui.teladialogo.DialogoTarefaController;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class DialogoTarefaView extends JDialog {

    private static final DateTimeFormatter formatadorDeData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private boolean salvo = false;

    private JTextField campoTitulo;
    private JTextField campoDescricao;
    private JSpinner campoPrioridade;
    private JTextField campoPrazo;
    private JButton botaoSalvar;
    private JButton botaoCancelar;

    public DialogoTarefaView(Frame owner, String title, Tarefa tarefa) {
        super(owner, title, true);
        configurarLayout();
        if (tarefa != null) {
            preencherCampos(tarefa);
        }
    }

    private void configurarLayout() {
        setSize(1280, 720);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel labelTitulo = new JLabel("Título:");
        labelTitulo.setBounds(400, 230, 100, 30);
        add(labelTitulo);

        campoTitulo = new JTextField();
        campoTitulo.setBounds(600, 230, 400 , 30);
        add(campoTitulo);

        JLabel labelDescricao = new JLabel("Descrição:");
        labelDescricao.setBounds(400, 275, 100, 30);
        add(labelDescricao);

        campoDescricao = new JTextField();
        campoDescricao.setBounds(600, 275, 400, 30);
        add(campoDescricao);

        JLabel labelPrioridade = new JLabel("Prioridade:");
        labelPrioridade.setBounds(400, 320, 100, 30);
        add(labelPrioridade);

        campoPrioridade = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        campoPrioridade.setBounds(600, 320, 70, 30);
        add(campoPrioridade);

        JLabel labelPrazo = new JLabel("Prazo (dd/MM/yyyy):");
        labelPrazo.setBounds(400, 365, 150, 30);
        add(labelPrazo);

        campoPrazo = new JTextField();
        campoPrazo.setBounds(600, 365, 400, 30);
        add(campoPrazo);

        botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBounds(600, 425, 120, 30);
        add(botaoSalvar);

        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setBounds(750, 425, 120, 30);
        add(botaoCancelar);
    }

    private void preencherCampos(Tarefa tarefa) {
        campoTitulo.setText(tarefa.getTitulo());
        campoDescricao.setText(tarefa.getDescricao());
        campoPrioridade.setValue(tarefa.getPrioridade());
        campoPrazo.setText(tarefa.getDeadline().format(formatadorDeData));
    }

    public void setController(DialogoTarefaController controller) {
        botaoSalvar.addActionListener(e -> controller.salvar());
        botaoCancelar.addActionListener(e -> dispose());
    }

    // Getters para o Controller
    public String getTitulo() { return campoTitulo.getText().trim(); }
    public String getDescricao() { return campoDescricao.getText().trim(); }
    public int getPrioridade() { return (int) campoPrioridade.getValue(); }
    public String getPrazo() { return campoPrazo.getText(); }

    // Métodos para o Controller controlar a View
    public void exibirMensagem(String titulo, String mensagem, int tipo) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipo);
    }

    public void marcarComoSalvo() {
        this.salvo = true;
        dispose();
    }

    public boolean foiSalvo() {
        return this.salvo;
    }
}