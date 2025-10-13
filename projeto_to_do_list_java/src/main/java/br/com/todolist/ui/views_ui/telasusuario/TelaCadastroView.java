// Em: src/main/java/br/com/todolist/ui/views/telasusuario/TelaCadastroView.java
package br.com.todolist.ui.views_ui.telasusuario;

import br.com.todolist.ui.controllers.telasusuario.TelaCadastroController;
import javax.swing.*;
import java.awt.*;

public class TelaCadastroView extends JDialog {

    private JTextField campoNome;
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoCadastrar;
    private JButton botaoCancelar;

    public TelaCadastroView(Frame owner) {
        super(owner, "Criar Nova Conta", true);
        configurarLayout();
    }

    private void configurarLayout() {
        setSize(1280, 720);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);

        // Campo Nome
        JLabel labelNome = new JLabel("Nome:");
        labelNome.setBounds(440, 230, 100, 30);
        add(labelNome);

        campoNome = new JTextField();
        campoNome.setBounds(550, 230, 250, 30);
        add(campoNome);

        // Campo Email
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(440, 275, 100, 30);
        add(labelEmail);

        campoEmail = new JTextField();
        campoEmail.setBounds(550, 275, 250, 30);
        add(campoEmail);

        // Campo Senha
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(440, 320, 100, 30);
        add(labelSenha);

        campoSenha = new JPasswordField();
        campoSenha.setBounds(550, 320, 250, 30);
        add(campoSenha);

        // Botoes
        botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.setBounds(550, 380, 120, 30);
        add(botaoCadastrar);

        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setBounds(680, 380, 120, 30);
        add(botaoCancelar);
    }

    public void setController(TelaCadastroController controller) {
        botaoCadastrar.addActionListener(e -> controller.cadastrarUsuario());
        botaoCancelar.addActionListener(e -> dispose());
    }

    public String getNome() {
        return campoNome.getText();
    }

    public String getEmail() {
        return campoEmail.getText();
    }

    public String getSenha() {
        return new String(campoSenha.getPassword());
    }

    public void exibirMensagem(String titulo, String mensagem, int tipo) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipo);
    }

    public void fechar() {
        dispose();
    }
}