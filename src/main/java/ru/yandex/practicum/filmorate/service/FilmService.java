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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.*;

@Slf4j
@Service
public class FilmService extends ResourceService<Film, FilmStorage> {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final Comparator<Film> likeNumberComparator = Comparator.comparing(film -> -film.getLikes().size());

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
        storage.checkContains(filmId);
        userStorage.checkContains(userId);
        Film film = storage.get(filmId);
        if (film.containsLike(userId)) {
            log.warn(Messages.likeAlreadySet(filmId, userId));
            throw new ValidationException(HttpStatus.BAD_REQUEST, LIKE_ALREADY_SET);
        }
        film.addLike(userId);
    }

    public void deleteLike(int filmId, int userId) {
        storage.checkContains(filmId);
        userStorage.checkContains(userId);
        Film film = storage.get(filmId);
        if (!film.containsLike(userId)) {
            log.warn(Messages.likeNotSet(filmId, userId));
            throw new ValidationException(HttpStatus.BAD_REQUEST, LIKE_NOT_SET);
        }
        film.deleteLike(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return storage.getAll().stream()
                .sorted(likeNumberComparator)
                .limit(count)
                .collect(Collectors.toList());
    }
}
