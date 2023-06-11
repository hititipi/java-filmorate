package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FeedDbStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.utils.EventType;
import ru.yandex.practicum.filmorate.utils.Operation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeStorage likeStorage;
    private final FeedDbStorage feedDbStorage;

    public void addLike(Integer filmID, Integer userID) {
        likeStorage.addLike(filmID, userID);
        feedDbStorage.addEvent(new Event(userID, EventType.LIKE, Operation.ADD, filmID));
    }

    public void deleteLike(Integer filmID, Integer userID) {
        likeStorage.deleteLike(filmID, userID);
        feedDbStorage.addEvent(new Event(userID, EventType.LIKE, Operation.REMOVE, filmID));
    }

    public List<Film> getMostLikedFilms(Integer count) {
        return likeStorage.getMostLikedFilms(count);
    }

    public List<Film> getAllFilmsSortedByRating() {
        return likeStorage.getAllFilmsSortedByRating();
    }
}