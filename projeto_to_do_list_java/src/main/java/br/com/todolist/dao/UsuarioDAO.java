// package br.com.todolist.dao;

import br.com.todolist.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.Optional;

public class UsuarioDAO {

    public void salvar(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

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

    // ... outros métodos como update, delete, findAll ...
}