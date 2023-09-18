package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.SortBy;

import java.util.Collection;
import java.util.List;

public interface FilmService {

    Film getFilm(int id);

    Collection<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(int id);

    Collection<Film> getDirectorFilmsWithSort(int directorId, SortBy sortBy);

    List<Film> getCommonFilms(int userId, int friendId);

    List<Film> searchFilms(String query, String by);
}
