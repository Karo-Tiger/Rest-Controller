package ru.kata.spring.boot_security.demo.controller;


import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.UserDto;
import ru.kata.spring.boot_security.demo.servis.UserService;


import java.util.List;
@RestController
@RequestMapping("/api/admin/users")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.findAllDto();
    }

    @GetMapping("/roles")
    public List<Role> roles() {
        return userService.getAllRoles();
    }

    @PostMapping
    public void create(@RequestBody UserDto dto) {
        userService.createUser(dto);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody UserDto dto) {
        userService.updateUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}