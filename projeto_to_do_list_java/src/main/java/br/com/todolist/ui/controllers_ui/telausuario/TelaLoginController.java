
package br.com.todolist.ui.controllers_ui.telausuario;

import br.com.todolist.entity.Usuario;
import br.com.todolist.service.gerentes.GerenteDeUsuarios;
import br.com.todolist.ui.views_ui.telasusuario.TelaCadastroView;
import br.com.todolist.ui.views_ui.telasusuario.TelaLoginView;
import br.com.todolist.ui.views_ui.telaprincipal.TelaPrincipalView;
import br.com.todolist.ui.controllers_ui.telaprincipal.TelaPrincipalController;

public class TelaLoginController {

    private final TelaLoginView view;
    private final GerenteDeUsuarios gerenteDeUsuarios;

    public TelaLoginController(TelaLoginView view) {
        this.view = view;
        this.gerenteDeUsuarios = new GerenteDeUsuarios();
        this.view.setController(this);
    }

    public void realizarLogin() {
        String email = view.getEmail();
        String senha = view.getSenha();

        if (email.trim().isEmpty() || senha.trim().isEmpty()) {
            view.exibirMensagemDeErro("Email e senha são obrigatórios.");
            return;
        }

        Usuario usuarioAutenticado = gerenteDeUsuarios.autenticarUsuario(email, senha);

        if (usuarioAutenticado != null) {
            // Linha modificada para iniciar a nova estrutura MVC
            TelaPrincipalView telaPrincipalView = new TelaPrincipalView(usuarioAutenticado);
            new TelaPrincipalController(telaPrincipalView, usuarioAutenticado);
            telaPrincipalView.setVisible(true);
            view.dispose();
        } else {
            view.exibirMensagemDeErro("Email ou senha incorretos.");
        }
    }

    public void abrirTelaDeCadastro() {
        // Agora esta é a chamada correta
        TelaCadastroView telaCadastroView = new TelaCadastroView(view);
        // O GerenteDeUsuarios já foi instanciado neste controller, podemos reutilizá-lo.
        new TelaCadastroController(telaCadastroView, this.gerenteDeUsuarios);
        telaCadastroView.setVisible(true);
    }
}