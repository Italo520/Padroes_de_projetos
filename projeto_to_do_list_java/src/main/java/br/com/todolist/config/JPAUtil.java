// package br.com.todolist.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory FACTORY =
            Persistence.createEntityManagerFactory("todolist-jpa");

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }
}