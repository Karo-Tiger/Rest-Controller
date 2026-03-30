package ru.kata.spring.boot_security.demo.servis;

import ru.kata.spring.boot_security.demo.model.Person;

import java.util.List;

public interface PersonService {
    public Person findByUsername(Long id);
    public List<Person> findAll();
    public Person add(Person person);
    public void deleteById(Long id);
}
