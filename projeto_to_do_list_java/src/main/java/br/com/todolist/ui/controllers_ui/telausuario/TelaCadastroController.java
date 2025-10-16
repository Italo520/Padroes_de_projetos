package br.com.todolist.ui.controllers_ui.telausuario;

import br.com.todolist.service.gerentes.GerenteDeUsuarios;
import br.com.todolist.ui.views_ui.telasusuario.TelaCadastroView;
import javax.swing.JOptionPane;

public class TelaCadastroController {

    private final TelaCadastroView view;
    private final GerenteDeUsuarios gerenteDeUsuarios;

    public TelaCadastroController(TelaCadastroView view, GerenteDeUsuarios gerenteDeUsuarios) {
        this.view = view;
        this.gerenteDeUsuarios = gerenteDeUsuarios;
        this.view.setController(this);
    }

    public void cadastrarUsuario() {
        String nome = view.getNome().trim();
        String email = view.getEmail().trim();
        String senha = view.getSenha().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            view.exibirMensagem("Erro de Validação", "Todos os campos são obrigatórios.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean sucesso = gerenteDeUsuarios.criarNovoUsuario(nome, email, senha);

        if (sucesso) {
            view.exibirMensagem("Sucesso", "Usuário cadastrado com sucesso!", JOptionPane.INFORMATION_MESSAGE);
            view.fechar();
        } else {
            view.exibirMensagem("Erro no Cadastro", "Este email já está em uso. Tente outro.", JOptionPane.ERROR_MESSAGE);
        }
    }
}