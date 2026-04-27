package ru.kata.spring.boot_security.demo.servis;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.model.UserDto;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserDto> findAllDto();

    UserDto findDtoById(Long id);

    void createUser(UserDto dto);

    void updateUser(Long id, UserDto dto);

    void delete(Long id);

    List<Role> getAllRoles();

    Role findRoleById(Long id);

    UserDto getCurrentUserDto();
}