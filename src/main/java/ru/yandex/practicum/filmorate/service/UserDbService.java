package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FeedDbStorage;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.utils.EventType;
import ru.yandex.practicum.filmorate.utils.Operation;

import java.util.Collection;


@Slf4j
@Service
public class UserDbService extends ResourceService<User, UserDbStorage> {

    private final FriendStorage friendStorage;
    private final FeedDbStorage feedDbStorage;

    @Autowired
    public UserDbService(UserDbStorage storage, FriendStorage friendStorage, FeedDbStorage feedDbStorage) {
        this.storage = storage;
        this.friendStorage = friendStorage;
        this.feedDbStorage = feedDbStorage;
    }

    @Override
    public void validateResource(User resource) {

    }

    public void addFriend(int id, int friendId) {
        storage.checkContains(id);
        storage.checkContains(friendId);
        friendStorage.addFriend(id, friendId);
        feedDbStorage.addEvent(new Event(id, EventType.FRIEND, Operation.ADD, friendId));
    }

    public void deleteFriend(int id, int friendId) {
        friendStorage.deleteFriend(id, friendId);
        feedDbStorage.addEvent(new Event(id, EventType.FRIEND, Operation.REMOVE, friendId));
    }

    public Collection<User> getUserFriends(int id) {
        storage.checkContains(id);
        return friendStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        return friendStorage.getCommonFriends(id, otherId);
    }

    public Collection<Event> getFeed(int id) {
        storage.checkContains(id);
        return feedDbStorage.findUserFeed(id);
    }
}