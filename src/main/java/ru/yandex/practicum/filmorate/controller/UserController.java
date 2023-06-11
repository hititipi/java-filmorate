package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.service.UserDbService;
import ru.yandex.practicum.filmorate.utils.Messages;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User, UserDbService> {

    @Autowired
    public UserController(UserDbService service) {
        this.service = service;
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
    public User createResource(@Valid @RequestBody User user) {
        log.info(Messages.tryAddResource(user));
        return super.createResource(user);
    }

    @PutMapping
    public User updateResource(@Valid @RequestBody User user) {
        log.info(Messages.tryUpdateResource(user));
        return super.updateResource(user);
    }

    @DeleteMapping("{id}")
    public void deleteResource(@PathVariable int id) {
        log.info(Messages.deleteUser(id));
        service.deleteUser(id);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info(Messages.addFriend(id, friendId));
        service.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info(Messages.deleteFriend(id, friendId));
        service.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public Collection<User> getUserFriends(@PathVariable int id) {
        log.info(Messages.getFriends(id));
        return service.getUserFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info(Messages.getCommonFriends(id, otherId));
        return service.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable int id) {
        return service.getRecommendations(id);
    }

    @GetMapping("{id}/feed")
    public Collection<Event> getFeed(@PathVariable int id) {
        log.info(Messages.getFeed(id));
        return service.getFeed(id);
    }
}