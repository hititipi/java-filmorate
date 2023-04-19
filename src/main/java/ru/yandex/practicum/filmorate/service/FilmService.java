package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.Messages;

import java.time.LocalDate;
import java.util.List;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.FILM_RELEASE_INVALID;
import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@Slf4j
@Service
public class FilmService extends ResourceService<Film> {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage storage, UserStorage userStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
    }


    private void validateReleaseDate(LocalDate releaseDate) {
        if (releaseDate != null && MIN_RELEASE_DATE.isAfter(releaseDate)) {
            log.info(Messages.invalidReleaseDate(releaseDate));
            throw new ValidationException(HttpStatus.BAD_REQUEST, FILM_RELEASE_INVALID);
        }
    }

    @Override
    public void validateResource(Film film) {
        validateReleaseDate(film.getReleaseDate());
    }

    public void addLike(int filmId, int userId) {
        if (!storage.contains(filmId)) {
            log.warn(Messages.filmNotFound(filmId));
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        if (!userStorage.contains(userId)) {
            log.warn(Messages.userNotFound(userId));
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        Film film = storage.get(filmId);
        if (film.containsLike(userId)) {
            log.warn(Messages.likeAlreadySet(filmId, userId));
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Лайк уже поставлен");
        }
        film.addLike(userId);
    }

    public void deleteLike(int filmId, int userId) {
        if (!storage.contains(filmId)) {
            log.warn(Messages.filmNotFound(filmId));
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        if (!userStorage.contains(userId)) {
            log.warn(Messages.userNotFound(userId));
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        Film film = storage.get(filmId);
        if (!film.containsLike(userId)) {
            log.warn(Messages.likeNotSet(filmId, userId));
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Лайк не поставлен");
        }
        film.deleteLike(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return ((FilmStorage) storage).getMostPopular(count);
    }
}
