package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.Collection;


@Slf4j
@Service
public class UserDbService extends ResourceService<User, UserDbStorage> {

    private final FriendStorage friendStorage;

    @Autowired
    public UserDbService(UserDbStorage storage, FriendStorage friendStorage) {
        this.storage = storage;
        this.friendStorage = friendStorage;
    }

    @Override
    public void validateResource(User resource) {

    }

    public void addFriend(int id, int friendId) {
        storage.checkContains(id);
        storage.checkContains(friendId);
        friendStorage.addFriend(id, friendId);
    }

    public void deleteFriend(int id, int friendId) {
        friendStorage.deleteFriend(id, friendId);
    }

    public Collection<User> getUserFriends(int id) {
        storage.checkContains(id);
        return friendStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        return friendStorage.getCommonFriends(id, otherId);
    }
}