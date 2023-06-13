package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.FeedDbStorage;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.utils.EventType;
import ru.yandex.practicum.filmorate.utils.Operation;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDbStorage userStorage;
    private final LikeStorage likeStorage;
    private final FriendStorage friendStorage;
    private final FilmDbStorage filmDbStorage;
    private final FeedDbStorage feedDbStorage;

    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        userStorage.addUser(user);
        return user;
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    public void addFriend(int id, int friendId) {
        userStorage.checkContains(id);
        userStorage.checkContains(friendId);
        friendStorage.addFriend(id, friendId);
        feedDbStorage.addEvent(new Event(id, EventType.FRIEND, Operation.ADD, friendId));
    }

    public void deleteFriend(int id, int friendId) {
        friendStorage.deleteFriend(id, friendId);
        feedDbStorage.addEvent(new Event(id, EventType.FRIEND, Operation.REMOVE, friendId));
    }

    public Collection<User> getUserFriends(int id) {
        userStorage.checkContains(id);
        return friendStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        return friendStorage.getCommonFriends(id, otherId);
    }

    public Collection<Event> getFeed(int id) {
        userStorage.checkContains(id);
        return feedDbStorage.findUserFeed(id);
    }

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