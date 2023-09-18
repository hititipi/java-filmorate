package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.utils.Messages;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("{id}")
    public User getUser(@PathVariable int id) {
        log.info(Messages.getUser(id));
        return userService.getUser(id);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info(Messages.getAllUsers());
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info(Messages.addUser());
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info(Messages.updateUser(user.getId()));
        return userService.updateUser(user);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable int id) {
        log.info(Messages.deleteUser(id));
        userService.deleteUser(id);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info(Messages.addFriend(id, friendId));
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info(Messages.deleteFriend(id, friendId));
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public Collection<User> getUserFriends(@PathVariable int id) {
        log.info(Messages.getFriends(id));
        return userService.getUserFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info(Messages.getCommonFriends(id, otherId));
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable int id) {
        log.info(Messages.getRecommendation(id));
        return userService.getRecommendations(id);
    }

    @GetMapping("{id}/feed")
    public Collection<Event> getFeed(@PathVariable int id) {
        log.info(Messages.getFeed(id));
        return userService.getFeed(id);
    }
}