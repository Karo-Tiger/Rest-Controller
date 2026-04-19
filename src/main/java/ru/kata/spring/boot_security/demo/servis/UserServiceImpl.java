package ru.kata.spring.boot_security.demo.servis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;;
import ru.kata.spring.boot_security.demo.model.UserDto;
import ru.kata.spring.boot_security.demo.ripository.RoleRepository;
import ru.kata.spring.boot_security.demo.ripository.UserRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String input) {

        System.out.println("🔥 LOAD USER BY: " + input);

        User user = userRepository.findByEmail(input)
                .orElseGet(() -> userRepository.findByUsername(input)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")));

        // 🔥 ВАЖНО: принудительная инициализация ролей
        user.getRoles().size();

        System.out.println("USER FOUND: " + user.getUsername());
        System.out.println("ROLES: " + user.getRoles());

        return user;
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // ✅ СОЗДАНИЕ ПОЛЬЗОВАТЕЛЯ (ИСПРАВЛЕНО)
    @Override
    @Transactional
    public User createUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByRole("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
            user.setRoles(Set.of(defaultRole));
        }

        return userRepository.save(user);
    }

    @Override
    public Role findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }
    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public User getInfo() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    // ✅ ОБНОВЛЕНИЕ ПОЛЬЗОВАТЕЛЯ (ИСПРАВЛЕНО)
    @Override
    public void updateUser(Long id, UserDto uzer) {

        User user = userRepository.findById(uzer.getId())
                .orElseThrow();

        user.setUsername(uzer.getUsername());
        user.setEmail(uzer.getEmail());

        if (user.getPassword() != null && !uzer.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(uzer.getPassword()));
        }

        user.setRoles(
                new HashSet<>(
                        roleRepository.findAllById(
                                uzer.getRoleIds() == null ? List.of() : uzer.getRoleIds()
                        )
                )
        );


        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}