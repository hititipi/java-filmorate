package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.Messages;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.*;

@Slf4j
@Service
public class UserService extends ResourceService<User> {

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public UserStorage getStorage() {
        return ((UserStorage) storage);
    }

    @Override
    public void validateResource(User resource) {

    }

    public void addFriend(int id, int friendId) {
        if (!containsKey(id)) {
            log.warn(Messages.userNotFound(id));
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        if (!containsKey(friendId)) {
            log.warn(Messages.userNotFound(friendId));
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        User user = get(id);
        if (user.getFriends().contains(friendId)) {
            log.warn(Messages.usersAlreadyFriends(id, friendId));
            throw new ValidationException(HttpStatus.BAD_REQUEST, USERS_ALREADY_FRIENDS);
        }
        user.addFriend(friendId);
        User friendUser = get(friendId);
        friendUser.addFriend(id);
    }

    public void deleteFriend(int id, int friendId) {
        if (!containsKey(id)) {
            log.warn(Messages.userNotFound(id));
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        if (!containsKey(friendId)) {
            log.warn(Messages.userNotFound(friendId));
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        User user = get(id);
        if (!user.getFriends().contains(friendId)) {
            log.warn(Messages.usersNotFriends(id, friendId));
            throw new ValidationException(HttpStatus.BAD_REQUEST, USERS_NOT_FRIENDS);
        }
        user.deleteFriend(friendId);
        User friendUser = get(friendId);
        friendUser.deleteFriend(id);
    }

    public Collection<User> getUserFriends(int id) {
        if (!containsKey(id)) {
            log.warn(Messages.userNotFound(id));
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        return getStorage().getUserFriends(id);
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        if (!containsKey(id)) {
            log.warn(Messages.userNotFound(id));
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        if (!containsKey(otherId)) {
            log.warn(Messages.userNotFound(otherId));
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        return getStorage().getCommonFriends(id, otherId);
    }
}