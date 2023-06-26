package ru.kata.spring.boot_security.demo.service;


import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();
    boolean saveUser(User user);
    User getUser(long id);
    void deleteUser(long id);
    @Query("Select u from User u left join fetch u.roles where u.email=:email")
    User findByEmail(String email);
}
