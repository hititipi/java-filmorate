package ru.yandex.practicum.filmorate.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    // boolean contains(int id);

    // void checkContains(int id);

    Collection<User> getAllUsers();

    User getUser(int id);

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(int id);

}
