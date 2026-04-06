package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.servis.UserService;

import java.util.List;

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
        return "adding_new_user";
    }

    @PostMapping("/admin")
    public String addpersons(@ModelAttribute("user") User user) {
        userService.createUser(user);
        return "redirect:/admin";
    }

    @GetMapping("edit")
    public String edit(Model model, @RequestParam("id") Long id) {
        model.addAttribute("user", userService.findById(id));
        return "change_user";
    }

    @PostMapping("/update")
    public String update(
            @RequestParam("id") Long id, @ModelAttribute("user") User user) {
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
