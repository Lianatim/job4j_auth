package ru.job4j.auth.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.exception.ResourceNotFoundException;
import ru.job4j.auth.exception.UserAlreadyExistsException;
import ru.job4j.auth.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService people;
    private PasswordEncoder encoder;

    public PersonController(PersonService people,
                            PasswordEncoder encoder) {
        this.people = people;
        this.encoder = encoder;
    }

    @GetMapping("/all")
    public List<Person> findAll() {
        return this.people.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        Person person = this.people.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not exist with id: " + id));
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return new ResponseEntity<>(
                this.people.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        Person personUpdate = this.people.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Person not exist with id: " + person.getId()));
        this.people.save(personUpdate);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person personDelete = this.people.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not exist with id: " + id));
        this.people.delete(personDelete);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody Person person) {
        if (person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("Username and password mustn't be empty");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        try {
            people.save(person);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("Person with this id already exists");
        }
        return ResponseEntity.ok().build();
    }
}