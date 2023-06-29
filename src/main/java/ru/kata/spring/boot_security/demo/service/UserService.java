package ru.kata.spring.boot_security.demo.service;


import org.springframework.data.jpa.repository.Query;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    void saveUser(User user);
    void updateUser(User user);

    User getUser(long id);

    void deleteUser(long id);

    @Query("Select u from User u left join fetch u.roles where u.email=:email")
    User findByEmail(String email);
}
