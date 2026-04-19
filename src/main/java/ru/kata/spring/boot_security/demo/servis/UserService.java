package ru.kata.spring.boot_security.demo.servis;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.model.UserDto;

import java.util.List;


public interface UserService extends UserDetailsService {

    List<User> findAll();

    User findById(Long id);

    User createUser(User user);

    User getInfo();

    void save(User user);
    Role findRoleById(Long id);

    List<Role> getAllRoles();


    // ✅ ОБНОВЛЕНИЕ ПОЛЬЗОВАТЕЛЯ (ИСПРАВЛЕНО)
    void updateUser(Long id, UserDto uzer);

    void delete(Long id);
}
