package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.model.UserDto;
import ru.kata.spring.boot_security.demo.servis.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminControl {
    private final UserService userService;

    @Autowired
    public AdminControl(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", userService.getAllRoles());
        return "list_of_users";
    }

    @GetMapping("/solo")
    public String index2(Model model, @RequestParam("id") Long id) {
        model.addAttribute("user", userService.findById(id));
        return "user_data";
    }

    @GetMapping("/add")
    public String addpers(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", userService.getAllRoles());
        return "adding_new_user";
    }

    @PostMapping()
    public String addpersons(@ModelAttribute("user") User user,
                             @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {

        Set<Role> roles = new HashSet<>();

        if (roleIds != null) {
            for (Long id : roleIds) {
                Role role = userService.findRoleById(id);
                roles.add(role);
            }
        }

        user.setRoles(roles);

        userService.createUser(user);

        return "redirect:/admin";
    }

    @GetMapping("edit")
    public String edit(Model model, @RequestParam("id") Long id) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("allRoles", userService.getAllRoles());
        return "change_user";
    }

    @PostMapping("/update")
    public String update(
            @ModelAttribute("user") UserDto userDto,
            @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {

        userDto.setRoleIds(roleIds == null ? List.of() : roleIds);

        userService.updateUser(userDto.getId(), userDto);

        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
