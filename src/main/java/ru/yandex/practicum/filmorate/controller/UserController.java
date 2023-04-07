package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.utils.Messages;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {

    @Override
    @GetMapping
    public Collection<User> getAll() {
        log.info(Messages.getAllUsers(resources.size()));
        return super.getAll();
    }

    @Override
    @PostMapping
    public User createResource(@Valid @RequestBody User user) {
        log.info(Messages.tryAddResource(user));
        return super.createResource(user);
    }


    @Override
    @PutMapping
    public User updateResource(@Valid @RequestBody User user) {
        log.info(Messages.tryUpdateResource(user));
        return super.updateResource(user);
    }

    @Override
    void validateResource(User resource) {

    }

}