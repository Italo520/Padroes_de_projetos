// Em: src/main/java/br/com/todolist/ui/views/teladialogo/DialogoEventoView.java
package br.com.todolist.ui.views_ui.teladialogo;

import br.com.todolist.entity.Evento;
import br.com.todolist.ui.controllers.teladialogo.DialogoEventoController;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class DialogoEventoView extends JDialog {

    private static final DateTimeFormatter formatadorDeData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private boolean salvo = false;

    private JTextField campoTitulo;
    private JTextField campoDescricao;
    private JTextField campoData;
    private JButton botaoSalvar;
    private JButton botaoCancelar;

    public DialogoEventoView(Frame owner, String title, Evento evento) {
        super(owner, title, true);
        configurarLayout();
        if (evento != null) {
            preencherCampos(evento);
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

        JLabel labelPrazo = new JLabel("Deadline (dd/MM/yyyy):");
        labelPrazo.setBounds(400, 320, 150, 30);
        add(labelPrazo);

        campoData = new JTextField();
        campoData.setBounds(600, 320, 400, 30);
        add(campoData);

        botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBounds(600, 425, 120, 30);
        add(botaoSalvar);

        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setBounds(750, 425, 120, 30);
        add(botaoCancelar);
    }

    private void preencherCampos(Evento evento) {
        campoTitulo.setText(evento.getTitulo());
        campoDescricao.setText(evento.getDescricao());
        campoData.setText(evento.getDeadline().format(formatadorDeData));
    }

    public void setController(DialogoEventoController controller) {
        botaoSalvar.addActionListener(e -> controller.salvar());
        botaoCancelar.addActionListener(e -> dispose());
    }

    // Getters para o Controller
    public String getTitulo() { return campoTitulo.getText().trim(); }
    public String getDescricao() { return campoDescricao.getText().trim(); }
    public String getData() { return campoData.getText(); }

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