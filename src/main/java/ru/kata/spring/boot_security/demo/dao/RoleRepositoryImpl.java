package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;

import javax.persistence.EntityManager;

@Repository
public class RoleRepositoryImpl implements RoleRepository {
    private final EntityManager entityManager;

    public RoleRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Role findByName(String name) {
        return entityManager.createQuery("select u from Role u where u.name = :name", Role.class)
                .setParameter("name", name).getSingleResult();
    }
}
