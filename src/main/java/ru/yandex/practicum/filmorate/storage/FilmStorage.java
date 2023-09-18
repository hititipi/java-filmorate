package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.SortBy;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    boolean contains(int filmId);

    Collection<Film> getAllFilms();

    Film getFilm(int id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(int id);

    void deleteFilmGenre(int id);

    void updateFilmGenre(Film updateFilm);

    void setFilmGenre(Film film);

    Film loadFilmGenre(Film film);

    List<Film> loadFilmGenres(List<Film> filmLIst);

    List<Film> searchFilmByTitle(String query);

    List<Film> searchFilmByDirector(String query);

    List<Film> searchFilmByTitleAndDirector(String query);

    List<Film> findDirectorFilmsWithSort(int directorId, SortBy sortBy);

    List<Film> getCommonFilms(int userId, int friendId);

    List<Film> getByListId(List<Integer> recommendations);
}
