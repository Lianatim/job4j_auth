package ru.job4j.auth.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.UserStore;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStore userStore;
    private final BCryptPasswordEncoder encoder;

    public UserController(UserStore userStore, BCryptPasswordEncoder encoder) {
        this.userStore = userStore;
        this.encoder = encoder;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        userStore.save(person);
    }

    @GetMapping("/all")
    public List<Person> findAll() {
        return userStore.findAll();
    }

}
