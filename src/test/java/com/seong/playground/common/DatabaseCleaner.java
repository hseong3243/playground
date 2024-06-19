package com.seong.playground.common;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.Type;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DatabaseCleaner {
    private final EntityManager entityManager;
    private final List<String> tableNames;

    public DatabaseCleaner(EntityManager em) {
        this.entityManager = em;
        tableNames = entityManager.getMetamodel()
            .getEntities()
            .stream()
            .map(Type::getJavaType)
            .filter(j -> !j.isAnnotationPresent(DiscriminatorValue.class))
            .map(javaType -> javaType.getAnnotation(Table.class))
            .map(Table::name)
            .toList();
    }

    public void clear() {
        entityManager.flush();
        entityManager.createNativeQuery("set foreign_key_checks = off")
            .executeUpdate();

        for (String tableName : tableNames) {
            entityManager.createNativeQuery("truncate table " + tableName)
                .executeUpdate();
        }

        entityManager.createNativeQuery("set foreign_key_checks = on")
            .executeUpdate();
    }
}
