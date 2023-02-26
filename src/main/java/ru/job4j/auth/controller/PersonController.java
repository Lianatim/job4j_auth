package ru.job4j.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.exception.ResourceNotFoundException;
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
        var person = this.people.findById(id);
        return new ResponseEntity<>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
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
    public void signUp(@RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        people.save(person);
    }
}