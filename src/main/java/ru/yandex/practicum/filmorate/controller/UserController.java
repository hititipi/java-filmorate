package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.Valid;
import java.util.*;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private static int idCounter = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            log.warn("Попытка создания уже существующего пользователя.  id = " + user.getId());
            throw new ValidationException(USER_ALREADY_EXISTS);
        }
        user.setId(idCounter++);
        users.put(user.getId(), user);
        log.info("Создан пользователь " + user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Обновлен пользователь " + user.getId());
        } else {
            log.warn("Попытка обновления не существующего пользователя.  id = " + user.getId());
            throw new ValidationException(USER_NOT_FOUND);
        }
        return user;
    }

}
