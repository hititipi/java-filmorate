package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.LikeService;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.utils.EventType;
import ru.yandex.practicum.filmorate.utils.Operation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeStorage likeStorage;
    private final FeedStorage feedDbStorage;
    private final FilmStorage filmDbStorage;
    private final DirectorStorage directorStorage;

    @Override
    public void addLike(int filmID, int userID) {
        likeStorage.addLike(filmID, userID);
        feedDbStorage.addEvent(new Event(userID, EventType.LIKE, Operation.ADD, filmID));
    }

    @Override
    public void deleteLike(int filmID, int userID) {
        likeStorage.deleteLike(filmID, userID);
        feedDbStorage.addEvent(new Event(userID, EventType.LIKE, Operation.REMOVE, filmID));
    }


    @Override
    public List<Film> getMostLikedFilms(int count, int genreId, int year) {
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

    @Override
    public List<Film> getAllFilmsSortedByRating() {
        return likeStorage.getAllFilmsSortedByRating();
    }
}