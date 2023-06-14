package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.utils.Messages;
import ru.yandex.practicum.filmorate.utils.SortBy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
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

    @Override
    public Film getFilm(int id) {
        Film film = filmStorage.getFilm(id);
        film = filmStorage.loadFilmGenre(film);
        film = directorStorage.loadFilmDirectors(film);
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        Collection<Film> films = filmStorage.getAllFilms();
        List<Film> filmLIst = new ArrayList<>(films);
        filmLIst = filmStorage.loadFilmGenres(filmLIst);
        filmLIst = directorStorage.loadFilmDirectors(filmLIst);
        return filmLIst;
    }

    @Override
    public Film createFilm(Film film) {
        validateFilm(film);
        film = filmStorage.createFilm(film);
        filmStorage.setFilmGenre(film);
        directorStorage.setFilmDirectors(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validateFilm(film);
        Film updateFilm = filmStorage.updateFilm(film);
        filmStorage.updateFilmGenre(updateFilm);
        directorStorage.updateFilmDirectors(updateFilm);
        return updateFilm;
    }

    @Override
    public void deleteFilm(int id) {
        filmStorage.deleteFilm(id);
        filmStorage.deleteFilmGenre(id);
        directorStorage.deleteFilmDirectors(id);
    }

    @Override
    public List<Film> getDirectorFilmsWithSort(int directorId, SortBy sortBy) {
        if (directorStorage.findById(directorId) != null) {
            List<Film> films = filmStorage.findDirectorFilmsWithSort(directorId, sortBy);
            films = filmStorage.loadFilmGenres(films);
            films = directorStorage.loadFilmDirectors(films);
            return films;
        } else throw new ValidationException(HttpStatus.BAD_REQUEST, RESOURCE_NOT_FOUND);
    }

    @Override
    public List<Film> searchFilms(String query, String by) {
        switch (by) {
            case "director,title":
            case "title,director":
                return filmStorage.searchFilmByTitleAndDirector(query);
            case "title":
                return filmStorage.searchFilmByTitle(query);
            case "director":
                return filmStorage.searchFilmByDirector(query);
            default:
                throw new ValidationException(HttpStatus.BAD_REQUEST, ERROR_SEARCH_QUERY);
        }
    }

    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }
}