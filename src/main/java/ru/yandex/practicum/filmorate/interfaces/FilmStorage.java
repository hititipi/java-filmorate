package ru.yandex.practicum.filmorate.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film getFilm(int id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(int id);
}
