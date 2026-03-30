package ru.kata.spring.boot_security.demo.ripository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.model.Person;

public interface PersonRepository extends JpaRepository<Person,Long> {
}
