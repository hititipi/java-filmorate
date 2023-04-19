package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.utils.Messages;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    private UserService getService() {
        return (UserService) service;
    }

    @GetMapping("{id}")
    public User get(@PathVariable int id) {
        log.info(Messages.getUser(id));
        return super.get(id);
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info(Messages.getAllUsers());
        return super.getAll();
    }

    @PostMapping
    public User createResource(@Valid @RequestBody User film) {
        log.info(Messages.tryAddResource(film));
        return super.createResource(film);
    }

    @PutMapping
    public User updateResource(@Valid @RequestBody User user) {
        log.info(Messages.tryUpdateResource(user));
        return super.updateResource(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info(Messages.addFriend(id, friendId));
        getService().addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info(Messages.deleteFriend(id, friendId));
        getService().deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public Collection<User> getUserFriends(@PathVariable int id) {
        log.info(Messages.getFriends(id));
        return getService().getUserFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info(Messages.getCommonFriends(id, otherId));
        return getService().getCommonFriends(id, otherId);
    }

}