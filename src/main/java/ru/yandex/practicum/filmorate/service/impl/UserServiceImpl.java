package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.utils.EventType;
import ru.yandex.practicum.filmorate.utils.Operation;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final FriendStorage friendStorage;
    private final FilmStorage filmDbStorage;
    private final FeedStorage feedDbStorage;

    @Override
    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User createUser(User user) {
        userStorage.addUser(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    @Override
    public void addFriend(int id, int friendId) {
        userStorage.checkContains(id);
        userStorage.checkContains(friendId);
        friendStorage.addFriend(id, friendId);
        feedDbStorage.addEvent(new Event(id, EventType.FRIEND, Operation.ADD, friendId));
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        friendStorage.deleteFriend(id, friendId);
        feedDbStorage.addEvent(new Event(id, EventType.FRIEND, Operation.REMOVE, friendId));
    }

    @Override
    public Collection<User> getUserFriends(int id) {
        userStorage.checkContains(id);
        return friendStorage.getFriends(id);
    }

    @Override
    public Collection<User> getCommonFriends(int id, int otherId) {
        return friendStorage.getCommonFriends(id, otherId);
    }

    @Override
    public Collection<Event> getFeed(int id) {
        userStorage.checkContains(id);
        return feedDbStorage.findUserFeed(id);
    }

    @Override
    public List<Film> getRecommendations(int userId) {
        userStorage.checkContains(userId);
        Map<Integer, List<Integer>> likes = likeStorage.getSameLikesByUser(userId);
        List<Integer> recommendations = computeRecommendations(likes.remove(userId), likes.values());
        return recommendations == null ? Collections.emptyList() : filmDbStorage.getByListId(recommendations);
    }

    private List<Integer> computeRecommendations(List<Integer> userFilms, Collection<List<Integer>> otherUserFilms) {
        int maxSameLikes = - 1;
        List<Integer> recommendations = null;
        for (List<Integer> films : otherUserFilms) {
            int size = films.size();
            films.removeAll(userFilms);
            if (size - films.size() > maxSameLikes && !films.isEmpty()) {
                maxSameLikes = size - films.size();
                recommendations = films;
            }
        }
        return recommendations;
    }
}