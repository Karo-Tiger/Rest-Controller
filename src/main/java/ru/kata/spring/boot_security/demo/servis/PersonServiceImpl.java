package ru.kata.spring.boot_security.demo.servis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Person;
import ru.kata.spring.boot_security.demo.ripository.PersonRepository;

import java.util.List;
@Service

public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    public Person findByUsername(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }
    public Person add(Person person) {
        return personRepository.save(person);
    }
    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }
}