// Em: src/main/java/br/com/todolist/ui/views/telasusuario/TelaLoginView.java
package br.com.todolist.ui.views.telasusuario;

import br.com.todolist.ui.controllers.telasusuario.TelaLoginController;
import javax.swing.*;

public class TelaLoginView extends JFrame {

    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;
    private JButton botaoCriarConta;

    public TelaLoginView() {
        super("Login - ToDo List");
        configurarLayout();
        setVisible(true);
    }

    private void configurarLayout() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        // Campo de Email
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(440, 260, 100, 30);
        add(labelEmail);

        campoEmail = new JTextField();
        campoEmail.setBounds(550, 260, 250, 30);
        add(campoEmail);

        // Campo de Senha
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(440, 305, 100, 30);
        add(labelSenha);

        campoSenha = new JPasswordField();
        campoSenha.setBounds(550, 305, 250, 30);
        add(campoSenha);

        // Botoes
        botaoEntrar = new JButton("Entrar");
        botaoEntrar.setBounds(550, 365, 120, 30);
        add(botaoEntrar);

        botaoCriarConta = new JButton("Criar Conta");
        botaoCriarConta.setBounds(680, 365, 120, 30);
        add(botaoCriarConta);
    }

    public void setController(TelaLoginController controller) {
        botaoEntrar.addActionListener(e -> controller.realizarLogin());
        campoSenha.addActionListener(e -> controller.realizarLogin());
        botaoCriarConta.addActionListener(e -> controller.abrirTelaDeCadastro());
    }

    public String getEmail() {
        return campoEmail.getText();
    }

    public String getSenha() {
        return new String(campoSenha.getPassword());
    }

    public void exibirMensagemDeErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro de Login", JOptionPane.ERROR_MESSAGE);
    }
}