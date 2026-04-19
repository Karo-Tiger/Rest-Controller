package ru.kata.spring.boot_security.demo.init;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.ripository.RoleRepository;
import ru.kata.spring.boot_security.demo.ripository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
public class Init {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Init(UserRepository userRepository,
                RoleRepository roleRepository,
                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        // Создаем роли, если их нет
        Role userRole = roleRepository.findByRole("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        Role adminRole = roleRepository.findByRole("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

        // Создаем пользователя "user", если его нет
        if (userRepository.findByEmail("user@example.com").isEmpty()) {
            User user = new User("user",
                    passwordEncoder.encode("user"),
                    "user@example.com"); // обязательное поле email
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
        }

        // Создаем пользователя "admin", если его нет
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User("admin",
                    passwordEncoder.encode("admin"),
                    "admin@example.com"); // обязательное поле email
            admin.setRoles(Set.of(userRole, adminRole));
            userRepository.save(admin);
        }
    }
    @PostConstruct
    public void init2() {

        String raw = "user";
        String encoded = passwordEncoder.encode(raw);

        System.out.println("ENCODED PASSWORD TEST: " + encoded);
        System.out.println("MATCH TEST: " + passwordEncoder.matches("user", encoded));
    }
}