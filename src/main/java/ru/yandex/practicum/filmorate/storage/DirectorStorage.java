package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface DirectorStorage {

    Director findById(int id);

    List<Director> findAll();

    Director add(Director director);

    void update(Director director);

    void delete(int id);

    void deleteFilmDirectors(int id);

    void updateFilmDirectors(Film updateFilm);

    void setFilmDirectors(Film film);

    Film loadFilmDirectors(Film film);

    List<Film> loadFilmDirectors(List<Film> films);

}
