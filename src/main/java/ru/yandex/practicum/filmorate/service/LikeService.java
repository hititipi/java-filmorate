package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.utils.EventType;
import ru.yandex.practicum.filmorate.utils.Operation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeStorage likeStorage;
    private final FeedDbStorage feedDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final DirectorStorage directorStorage;

    public void addLike(Integer filmID, Integer userID) {
        likeStorage.addLike(filmID, userID);
        feedDbStorage.addEvent(new Event(userID, EventType.LIKE, Operation.ADD, filmID));
    }

    public void deleteLike(Integer filmID, Integer userID) {
        likeStorage.deleteLike(filmID, userID);
        feedDbStorage.addEvent(new Event(userID, EventType.LIKE, Operation.REMOVE, filmID));
    }

    public List<Film> getMostLikedFilms(Integer count, int genreId, int year) {
        List<Film> films;
        if (genreId != 0 && year != 0) {
            films = likeStorage.getPopularByGenreAndYear(count, genreId, year);
        } else if (genreId != 0) {
            films = likeStorage.getPopularByGenre(count, genreId);
        } else if (year != 0) {
            films = likeStorage.getPopularByYear(count, year);
        } else {
            films = likeStorage.getMostLikedFilms(count);
        }
        return directorStorage.loadFilmDirectors(filmDbStorage.loadFilmGenres(films));
    }

    public List<Film> getAllFilmsSortedByRating() {
        return likeStorage.getAllFilmsSortedByRating();
    }
}