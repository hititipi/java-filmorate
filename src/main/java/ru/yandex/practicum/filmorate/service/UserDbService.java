package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
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

    @Autowired
    public UserDbService(UserDbStorage storage, LikeStorage likeStorage, FriendStorage friendStorage, FilmDbStorage filmDbStorage) {
        this.likeStorage = likeStorage;
        this.filmDbStorage = filmDbStorage;

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

    public List<Film> getRecommendations(int userId) {
        storage.checkContains(userId);
        log.info("Рекомендации фильмов от User с id = " + userId);
        // достаем всех пользователей у которых есть хотя бы одно пересечение по лайкам с текущим пользователем
        Map<Integer, List<Integer>> likes = likeStorage.getSameLikesByUser(userId);
        List<Integer> userFilms = likes.remove(userId);
        // если у текущего пользователя нет лайков или нет пересечений с другими, то и рекоммендаций быть не должно
        if (userFilms == null || likes.isEmpty()) return Collections.emptyList();
        // заполняем мапу с количеством пересечений (count:[userId]) с сортировкой по убыванию
        Map<Integer, List<Integer>> intersects = new TreeMap<>(Comparator.reverseOrder());
        for (Map.Entry<Integer, List<Integer>> entry : likes.entrySet()) {
            int intersectsCount = countingIntersects(userFilms, entry.getValue());
            intersects.putIfAbsent(intersectsCount, new ArrayList<>());
            intersects.get(intersectsCount).add(entry.getKey());
        }
        /* находим пользователя с максимальным количеством пересечений,
           если количество пересечений совпадает с количеством фильмов,
           то этот юзер нам не подходит (т.к. нечего рекомендовать), берем следующего
         */
        List<Integer> similarUserFilms = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : intersects.entrySet()) {
            for (Integer currentUserId : entry.getValue()) {
                List<Integer> currentUserFilms = likes.get(currentUserId);
                if (entry.getKey() < currentUserFilms.size()){
                    similarUserFilms = currentUserFilms;
                    break;
                }
            }
            if (!similarUserFilms.isEmpty()){
                break;
            }
        }
        // находим фильмы, которые не пролайкал текущий пользователь.
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
    public Collection<Event> getFeed(int id) {
        storage.checkContains(id);
        return feedDbStorage.findUserFeed(id);
    }
}