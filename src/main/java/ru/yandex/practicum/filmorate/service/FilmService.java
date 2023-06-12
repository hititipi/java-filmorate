package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.utils.Messages;

import java.time.LocalDate;
import java.util.*;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.FILM_RELEASE_INVALID;
import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final FilmDbStorage filmStorage;
    private final DirectorStorage directorStorage;

    private void validateReleaseDate(LocalDate releaseDate) {
        if (releaseDate != null && MIN_RELEASE_DATE.isAfter(releaseDate)) {
            log.info(Messages.invalidReleaseDate(releaseDate));
            throw new ValidationException(HttpStatus.BAD_REQUEST, FILM_RELEASE_INVALID);
        }
    }

    public void validateFilm(Film film) {
        validateReleaseDate(film.getReleaseDate());
    }

    public Film getFilm(int id) {
        Film film = filmStorage.getFilm(id);
        film = filmStorage.loadFilmGenre(film);
        film = directorStorage.loadFilmDirectors(film);
        return film;
    }

    public Collection<Film> getAllFilms() {
        Collection<Film> films = filmStorage.getAllFilms();
        List<Film> filmLIst = new ArrayList<>(films);
        filmLIst = filmStorage.loadFilmGenres(filmLIst);
        filmLIst = directorStorage.loadFilmDirectors(filmLIst);
        return filmLIst;
    }

    public Film createFilm(Film film) {
        validateFilm(film);
        film = filmStorage.createFilm(film);
        filmStorage.setFilmGenre(film);
        directorStorage.setFilmDirectors(film);
        return film;
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        Film updateFilm = filmStorage.updateFilm(film);
        filmStorage.updateFilmGenre(updateFilm);
        directorStorage.updateFilmDirectors(updateFilm);
        return updateFilm;
    }

    public void deleteFilm(int id) {
        filmStorage.deleteFilm(id);
        filmStorage.deleteFilmGenre(id);
        directorStorage.deleteFilmDirectors(id);
    }

    public List<Film> getDirectorFilmsWithSort(int directorId, String sortBy) {
        if (directorStorage.findById(directorId) != null) {
            List<Film> films = filmStorage.findDirectorFilmsWithSort(directorId, sortBy);
            films = filmStorage.loadFilmGenres(films);
            films = directorStorage.loadFilmDirectors(films);
            return films;
        } else throw new ValidationException(HttpStatus.BAD_REQUEST, RESOURCE_NOT_FOUND);
    }

    public List<Film> searchFilms(String query, String by) {
        List<Film> films = new ArrayList<>();

        if (by.split(",").length == 2) {
            films.addAll(filmStorage.searchFilmByTitleAndDirector(query));
        } else if (by.equals("title")) {
            films.addAll(filmStorage.searchFilmByTitle(query));
        } else if (by.equals("director")) {
            films.addAll(filmStorage.searchFilmByDirector(query));
        }
        return films;
    }

    public List<Film> getCommonFilms(int userId, int friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }
}