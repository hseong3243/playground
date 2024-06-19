package com.seong.playground.common;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
public abstract class BaseIntegrationTest {

    protected EntityManager em;

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    protected TransactionTemplate transaction;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        this.em = emf.createEntityManager();
        databaseCleaner.clear();
    }

    protected void persist(Object... entities) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        for (Object entity : entities) {
            if (entity instanceof List<?> list) {
                for (Object o : list) {
                    em.persist(o);
                }
            } else {
                em.persist(entity);
            }
        }
        tx.commit();
    }
}
