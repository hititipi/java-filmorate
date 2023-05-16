package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.Messages;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.*;

@Slf4j
@Service
public class UserService extends ResourceService<User, UserStorage> {

    @Autowired
    public UserService(@Qualifier("inMemoryUserStorage") UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public void validateResource(User resource) {

    }

    public void addFriend(int id, int friendId) {
        storage.checkContains(id);
        storage.checkContains(friendId);
        User user = get(id);
        if (user.hasFriend(friendId)) {
            log.warn(Messages.usersAlreadyFriends(id, friendId));
            throw new ValidationException(HttpStatus.BAD_REQUEST, USERS_ALREADY_FRIENDS);
        }
        user.addFriend(friendId);
        User friendUser = get(friendId);
        friendUser.addFriend(id);
    }

    public void deleteFriend(int id, int friendId) {
        storage.checkContains(id);
        storage.checkContains(friendId);
        User user = get(id);
        User friendUser = get(friendId);
        if (!user.hasFriend(friendId)) {
            log.warn(Messages.usersNotFriends(id, friendId));
            throw new ValidationException(HttpStatus.BAD_REQUEST, USERS_NOT_FRIENDS);
        }
        user.deleteFriend(friendId);
        friendUser.deleteFriend(id);
    }

    public Collection<User> getUserFriends(int id) {
        storage.checkContains(id);
        Set<Integer> friends = storage.get(id).getFriends();
        return friends.stream().map(this::get).collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        Collection<User> friends = getUserFriends(id);
        Collection<User> userFriends = getUserFriends(otherId);
        return friends.stream()
                .filter(userFriends::contains)
                .collect(Collectors.toList());
    }
}