package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendStorage {

    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    Collection<User> getFriends(int id);

    Collection<User> getCommonFriends(int id, int otherId);
}
