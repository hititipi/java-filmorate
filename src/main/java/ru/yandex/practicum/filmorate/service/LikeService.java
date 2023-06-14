package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeService {
    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> getMostLikedFilms(int count, int genreId, int year);

    List<Film> getAllFilmsSortedByRating();
}
