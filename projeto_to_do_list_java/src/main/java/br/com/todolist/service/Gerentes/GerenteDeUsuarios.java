package br.com.todolist.service.Gerentes;

import br.com.todolist.dao.UsuarioDAO;
import br.com.todolist.entity.Usuario;

public class GerenteDeUsuarios {

    private final UsuarioDAO usuarioDAO;

    public GerenteDeUsuarios() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public boolean criarNovoUsuario(String nome, String email, String password) {
        // Verifica se o usuário já existe antes de tentar criar
        if (usuarioDAO.findByEmail(email).isPresent()) {
            System.err.println("Erro: Email já cadastrado.");
            return false;
        }

        // A lógica de hash da senha já é tratada pelo DAO
        Usuario novoUsuario = new Usuario(nome, email, password);
        usuarioDAO.salvar(novoUsuario);
        return true;
    }

    public Usuario autenticarUsuario(String email, String password) {
        // A lógica de autenticação é delegada ao DAO
        Usuario usuario = usuarioDAO.autenticar(email, password);
        if (usuario != null) {
            System.out.println("Autenticação bem-sucedida para " + usuario.getNome());
        } else {
            System.err.println("Erro: Email ou senha incorretos.");
        }
        return usuario;
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        // Retorna o usuário se presente, ou null se ausente
        return usuarioDAO.findByEmail(email).orElse(null);
    }
}