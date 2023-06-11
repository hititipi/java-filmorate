package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserDbService extends ResourceService<User, UserDbStorage> {

    private final LikeStorage likeStorage;
    private final FriendStorage friendStorage;
    private final FilmDbStorage filmDbStorage;
    private final FeedDbStorage feedDbStorage;

    @Autowired
    public UserDbService(UserDbStorage storage, LikeStorage likeStorage, FriendStorage friendStorage, FilmDbStorage filmDbStorage, FeedDbStorage feedDbStorage) {
        this.likeStorage = likeStorage;
        this.filmDbStorage = filmDbStorage;
        this.storage = storage;
        this.friendStorage = friendStorage;
        this.feedDbStorage = feedDbStorage;
    }

    public void deleteUser(int id) {
        storage.delete(id);
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

    public List<Film> getRecommendations(int userId) {
        storage.checkContains(userId);
        log.info("Recommendations Films от User с id = " + userId);
        Map<Integer, List<Integer>> likes = likeStorage.getSameLikesByUser(userId);
        List<Integer> userFilms = likes.remove(userId);
        if (userFilms == null || likes.isEmpty()) return Collections.emptyList();
        Map<Integer, List<Integer>> intersects = new TreeMap<>(Comparator.reverseOrder());
        for (Map.Entry<Integer, List<Integer>> entry : likes.entrySet()) {
            int intersectsCount = countingIntersects(userFilms, entry.getValue());
            intersects.putIfAbsent(intersectsCount, new ArrayList<>());
            intersects.get(intersectsCount).add(entry.getKey());
        }
        List<Integer> similarUserFilms = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : intersects.entrySet()) {
            for (Integer currentUserId : entry.getValue()) {
                List<Integer> currentUserFilms = likes.get(currentUserId);
                if (entry.getKey() < currentUserFilms.size()) {
                    similarUserFilms = currentUserFilms;
                    break;
                }
            }
            if (!similarUserFilms.isEmpty()) {
                break;
            }
        }
        List<Integer> difference = findDifference(userFilms, similarUserFilms);
        return filmDbStorage.getByListId(difference);
    }

    private int countingIntersects(List<Integer> userFilm, List<Integer> anotherFilms) {
        int count = 0;
        for (Integer filmId : userFilm) {
            if (anotherFilms.contains(filmId)) count++;
        }
        return count;
    }

    private List<Integer> findDifference(List<Integer> userFilms, List<Integer> anotherFilms) {
        List<Integer> diff = new ArrayList<>();
        for (Integer filmId : anotherFilms) {
            if (!userFilms.contains(filmId)) diff.add(filmId);
        }
        return diff;
    }
}