package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserDaoImpl implements UserDao {
    private final EntityManager entityManager;
    private final RoleService roleService;

    public UserDaoImpl(EntityManager entityManager, RoleService roleService) {
        this.entityManager = entityManager;
        this.roleService = roleService;
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("Select u from User u left join fetch u.roles", User.class)
                .getResultList();
    }

    @Override
    public void saveUser(User user, List<Long> checkedIdRoles) {
        Set<Role> roles = new HashSet<>();
        for (Long id : checkedIdRoles) {
            roles.add(roleService.getRole(id));
        }
        user.setRoles(roles);
        entityManager.persist(user);
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);

    }

    @Override
    public User getUser(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void deleteUser(long id) {
        entityManager.createQuery("delete from User where id = :userId")
                .setParameter("userId", id)
                .executeUpdate();
    }

    @Override
    public User findByEmail(String email) {
        return entityManager.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email).getSingleResult();
    }
}
