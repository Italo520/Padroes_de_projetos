package br.com.todolist.dao;

import br.com.todolist.config.JPAUtil;
import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Usuario;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EventoDAO {

    /**
     * Salva um novo evento ou atualiza um existente.
     */
    public void salvar(Evento evento) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (evento.getId() == null) {
                // Se é um evento novo, o JPA gerencia a associação
                em.persist(evento);
            } else {
                // Se é um evento existente, atualiza
                em.merge(evento);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Busca um evento pelo seu ID.
     */
    public Optional<Evento> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Evento evento = em.find(Evento.class, id);
            return Optional.ofNullable(evento);
        } finally {
            em.close();
        }
    }

    /**
     * Busca todos os eventos associados a um usuário específico.
     */
    public List<Evento> buscarPorUsuario(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            return Collections.emptyList();
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // JPQL para selecionar eventos onde o ID do usuário corresponde
            String jpql = "SELECT e FROM Evento e WHERE e.usuario.id = :usuarioId";
            return em.createQuery(jpql, Evento.class)
                    .setParameter("usuarioId", usuario.getId())
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Deleta um evento do banco de dados.
     */
    public void deletar(Evento evento) {
        if (evento == null || evento.getId() == null) {
            return;
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            // Garante que a entidade está no estado gerenciado antes de remover
            Evento eventoGerenciado = em.find(Evento.class, evento.getId());
            if (eventoGerenciado != null) {
                em.remove(eventoGerenciado);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}