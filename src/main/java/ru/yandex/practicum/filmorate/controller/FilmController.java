package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static int idCounter = 1;

    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) throws ValidationException {
        validateFilm(film);
        if (films.containsKey(film.getId())) {
            log.warn("Попытка создания уже существующего фильма.  id = " + film.getId());
            throw new ValidationException(FILM_ALREADY_EXISTS);
        }
        film.setId(idCounter++);
        films.put(film.getId(), film);
        log.info("Создан фильм " + film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        validateFilm(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновлён фильм " + film.getId());
        } else {
            log.warn("Попытка обновления не существующего фильма.  id = " + film.getId());
            throw new ValidationException(FILM_NOT_FOUND);
        }
        return film;
    }

    private void validateReleaseDate(LocalDate releaseDate) throws ValidationException {
        if (releaseDate != null && MIN_RELEASE_DATE.isAfter(releaseDate)) {
            log.info("Некорректная дата релиза " + releaseDate);
            throw new ValidationException(FILM_RELEASE_INVALID);
        }
    }

    private void validateFilmDuration(Duration duration) throws ValidationException {
        if (duration != null && (duration.isNegative() || duration.isZero())) {
            log.info("Некорректная продолжительность фильма " + duration);
            throw new ValidationException(FILM_DURATION_INVALID);
        }
    }

    public void validateFilm(Film film) throws ValidationException {
        validateReleaseDate(film.getReleaseDate());
        validateFilmDuration((film.getDuration()));
    }

}
