package ru.yandex.practicum.filmorate.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage extends ResourceStorage<User> {

    Collection<User> getUserFriends(int id);

    Collection<User> getCommonFriends(int id, int otherId);
}
