// package br.com.todolist.dao;

import br.com.todolist.entity.Tarefa;
import jakarta.persistence.EntityManager;
import java.util.List;

public class TarefaDAO {

    public void salvar(Tarefa tarefa) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(tarefa);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Tarefa> findByUsuarioEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Tarefa t WHERE t.criadoPor.email = :email", Tarefa.class)
                    .setParameter("email", email)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // ... outros métodos ...
}