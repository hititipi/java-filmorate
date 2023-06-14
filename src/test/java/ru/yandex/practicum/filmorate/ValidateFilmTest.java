package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.impl.FilmServiceImpl;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.exception.ValidationErrors.*;

public class ValidateFilmTest {

    private static Validator validator;

    private static FilmServiceImpl filmService;

    @BeforeAll
    public static void setUp() {
        filmService = new FilmServiceImpl(null, null);
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private List<String> getValidateErrorMsg(Film film) {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        return violations
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    @Test
    void createValidFilm() {
        Film film = new Film("film_name", "film_description", LocalDate.now(), 100);
        film.setMpa(new Mpa(1, "G"));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Ошибка валидации при проверке правильных параметров");
        assertDoesNotThrow(() -> filmService.validateFilm(film), "Ошибка валидации фильма в контролере");
    }

    @Test
    void createFilmWithNullName() {
        Film film = new Film(null, "film_description", LocalDate.now(), 100);
        film.setMpa(new Mpa(1, "G"));
        List<String> messages = getValidateErrorMsg(film);
        assertEquals(messages.size(), 2, "Неверное количество сообщений");
        assertTrue(messages.contains(FILM_NAME_NULL), "Неверное сообщение об ошибке");
        assertTrue(messages.contains(FILM_NAME_EMPTY), "Неверное сообщение об ошибке");
    }

    @Test
    void createFilmWithEmptyName() {
        Film film = new Film("", "film_description", LocalDate.now(), 100);
        film.setMpa(new Mpa(1, "G"));
        List<String> messages = getValidateErrorMsg(film);
        assertEquals(messages.size(), 1, "Неверное количество сообщений");
        assertTrue(messages.contains(FILM_NAME_EMPTY), "Неверное сообщение об ошибке");
    }

    @Test
    void createFilmWithMaxLengthDescription() {
        String description = "-".repeat(200);
        Film film = new Film("name", description, LocalDate.now(), 100);
        film.setMpa(new Mpa(1, "G"));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Ошибка валидации при проверке правильных параметров");
        assertDoesNotThrow(() -> filmService.validateFilm(film), "Ошибка валидации фильма в контролере");
    }

    @Test
    void createFilmWithInvalidDescription() {
        String description = "-".repeat(210);
        Film film = new Film("name", description, LocalDate.now(), 100);
        film.setMpa(new Mpa(1, "G"));
        List<String> messages = getValidateErrorMsg(film);
        assertEquals(messages.size(), 1, "Неверное количество сообщений");
        assertTrue(messages.contains(FILM_DESCRIPTION_INVALID), "Неверное сообщение об ошибке");
    }

    @Test
    void createFilmWithRelease28_12_1895() {
        LocalDate releaseDate = LocalDate.of(1895, 12, 28);
        Film film = new Film("name", "description", releaseDate, 100);
        film.setMpa(new Mpa(1, "G"));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Ошибка валидации при проверке правильных параметров");
        assertDoesNotThrow(() -> filmService.validateFilm(film), "Ошибка валидации фильма в контролере");
    }

    @Test
    void createFilmWithInvalidRelease() {
        LocalDate invalidReleaseDate = LocalDate.of(1880, 12, 28);
        Film film = new Film("name", "description", invalidReleaseDate, 100);
        film.setMpa(new Mpa(1, "G"));
        Exception exception = assertThrows(ValidationException.class,
                () -> filmService.validateFilm(film), "Ошибка валидации фильма в контролере");
        assertEquals(exception.getMessage(), FILM_RELEASE_INVALID, "Неверное сообщение об ошибке");
    }

    @Test
    void createFilmWithNullRelease() {
        Film film = new Film("name", "description", null, 100);
        film.setMpa(new Mpa(1, "G"));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Ошибка валидации при проверке правильных параметров");
        assertDoesNotThrow(() -> filmService.validateFilm(film), "Ошибка валидации фильма в контролере");
    }

    @Test
    void createFilmWithZeroDuration() {
        Film film = new Film("name", "description", LocalDate.now(), 0);
        film.setMpa(new Mpa(1, "G"));
        List<String> messages = getValidateErrorMsg(film);
        assertEquals(messages.size(), 1, "Неверное количество сообщений");
        assertTrue(messages.contains(FILM_DURATION_INVALID), "Неверное сообщение об ошибке");
    }

    @Test
    void createFilmWithNegativeDuration() {
        Film film = new Film("name", "description", LocalDate.now(), -10);
        film.setMpa(new Mpa(1, "G"));
        List<String> messages = getValidateErrorMsg(film);
        assertEquals(messages.size(), 1, "Неверное количество сообщений");
        assertTrue(messages.contains(FILM_DURATION_INVALID), "Неверное сообщение об ошибке");
    }
}
