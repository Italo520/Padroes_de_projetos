package br.com.todolist.dao;

import br.com.todolist.config.JPAUtil;
import br.com.todolist.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.Optional;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO {

    /**
     * Salva um novo usuário no banco de dados, com a senha criptografada.
     */
    public void salvar(Usuario usuario) {
        // Criptografa a senha antes de salvar no banco de dados
        String senhaHasheada = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());
        usuario.setPassword(senhaHasheada);

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Autentica um usuário, verificando email e senha.
     * @return O objeto Usuario se a autenticação for bem-sucedida, caso contrário, null.
     */
    public Usuario autenticar(String email, String senhaPura) {
        Optional<Usuario> usuarioOpt = findByEmail(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Verifica se a senha fornecida corresponde ao hash salvo no banco
            if (BCrypt.checkpw(senhaPura, usuario.getPassword())) {
                return usuario;
            }
        }
        // Retorna null se o email não for encontrado ou a senha estiver incorreta
        return null;
    }

    /**
     * Busca um usuário pelo seu endereço de email.
     * @return um Optional contendo o usuário se encontrado, ou um Optional vazio.
     */
    public Optional<Usuario> findByEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Usuario usuario = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.ofNullable(usuario);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }


}