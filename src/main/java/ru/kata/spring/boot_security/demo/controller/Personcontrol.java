package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Person;
import ru.kata.spring.boot_security.demo.servis.PersonServiceImpl;

@Controller
@RequestMapping("/admin")
public class Personcontrol {
    private final PersonServiceImpl personservis;
    @Autowired
    public Personcontrol(PersonServiceImpl personservis) {
        this.personservis = personservis;
    }
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("persons", personservis.findAll());
        return "person1";
    }
    @GetMapping("/solo")
    public String index2(Model model, @RequestParam("id") Long id) {
        model.addAttribute("persons", personservis.findByUsername(id));
        return "person2";
    }
    @GetMapping("/add")
    public String addpers(Model model) {
        model.addAttribute("persons", new Person());
        return "person3";
    }
    @PostMapping
    public String addpersons(@ModelAttribute("pers") Person person) {
        personservis.add(person);
        return "redirect:/admin";
    }
    @GetMapping("edit")
    public String edit(Model model, @RequestParam("id") Long id) {
        model.addAttribute("persons", personservis.findByUsername(id));
        return "person4";
    }
    @PostMapping("/update")
    public String update(
            @RequestParam("id") Long id, @ModelAttribute("pers") Person person) {
        person.setId(id);
        personservis.add(person);
        return "redirect:/admin";
    }
    @PostMapping("/delet")
    public String delete(@RequestParam("id") Long id) {
        personservis.deleteById(id);
        return "redirect:/admin";
    }
}
