package ru.kata.spring.boot_security.demo.servis;

import lombok.RequiredArgsConstructor;
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
import java.util.stream.Collectors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> findAllDto() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findDtoById(Long id) {
        return toDto(findByIdEntity(id));
    }

    @Override
    public void createUser(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(mapRoles(dto.getRoleIds()));
        userRepository.save(user);
    }

    @Override
    public void updateUser(Long id, UserDto dto) {
        User user = findByIdEntity(id);

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        user.setRoles(mapRoles(dto.getRoleIds()));
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role findRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow();
    }

    @Override
    public UserDto getCurrentUserDto() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new RuntimeException("Not authenticated");
        }

        return toDto(user);
    }

    private User findByIdEntity(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    private Set<Role> mapRoles(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(roleRepository.findAllById(ids));
    }

    private UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                null,
                user.getEmail(),
                user.getRoles().stream().map(Role::getRole).collect(Collectors.toList()),
                user.getRoles().stream().map(Role::getId).collect(Collectors.toList())
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}