package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.exception.ValidationErrors.*;

public class ValidateFilmTest {

    private static Validator validator;

    private static FilmService filmService;

    @BeforeAll
    public static void setUp() {
        filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
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
        Film film = new Film("film_name", "film_description", LocalDate.now(), Duration.ofMinutes(100));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Ошибка валидации при проверке правильных параметров");
        assertDoesNotThrow(() -> filmService.validateResource(film), "Ошибка валидации фильма в контролере");
    }

    @Test
    void createFilmWithNullName() {
        Film film = new Film(null, "film_description", LocalDate.now(), Duration.ofMinutes(100));
        List<String> messages = getValidateErrorMsg(film);
        assertEquals(messages.size(), 2, "Неверное количество сообщений");
        assertTrue(messages.contains(FILM_NAME_NULL), "Неверное сообщение об ошибке");
        assertTrue(messages.contains(FILM_NAME_EMPTY), "Неверное сообщение об ошибке");
    }

    @Test
    void createFilmWithEmptyName() {
        Film film = new Film("", "film_description", LocalDate.now(), Duration.ofMinutes(100));
        List<String> messages = getValidateErrorMsg(film);
        assertEquals(messages.size(), 1, "Неверное количество сообщений");
        assertTrue(messages.contains(FILM_NAME_EMPTY), "Неверное сообщение об ошибке");
    }

    @Test
    void createFilmWithMaxLengthDescription() {
        String description = "-".repeat(200);
        Film film = new Film("name", description, LocalDate.now(), Duration.ofMinutes(100));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Ошибка валидации при проверке правильных параметров");
        assertDoesNotThrow(() -> filmService.validateResource(film), "Ошибка валидации фильма в контролере");
    }

    @Test
    void createFilmWithInvalidDescription() {
        String description = "-".repeat(210);
        Film film = new Film("name", description, LocalDate.now(), Duration.ofMinutes(100));
        List<String> messages = getValidateErrorMsg(film);
        assertEquals(messages.size(), 1, "Неверное количество сообщений");
        assertTrue(messages.contains(FILM_DESCRIPTION_INVALID), "Неверное сообщение об ошибке");
    }

    @Test
    void createFilmWithRelease28_12_1895() {
        LocalDate releaseDate = LocalDate.of(1895, 12, 28);
        Film film = new Film("name", "description", releaseDate, Duration.ofMinutes(100));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Ошибка валидации при проверке правильных параметров");
        assertDoesNotThrow(() -> filmService.validateResource(film), "Ошибка валидации фильма в контролере");
    }

    @Test
    void createFilmWithInvalidRelease() {
        LocalDate invalidReleaseDate = LocalDate.of(1880, 12, 28);
        Film film = new Film("name", "description", invalidReleaseDate, Duration.ofMinutes(100));
        Exception exception = assertThrows(ValidationException.class,
                () -> filmService.validateResource(film), "Ошибка валидации фильма в контролере");
        assertEquals(exception.getMessage(), FILM_RELEASE_INVALID, "Неверное сообщение об ошибке");
    }

    @Test
    void createFilmWithNullRelease() {
        Film film = new Film("name", "description", null, Duration.ofMinutes(100));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Ошибка валидации при проверке правильных параметров");
        assertDoesNotThrow(() -> filmService.validateResource(film), "Ошибка валидации фильма в контролере");
    }

    @Test
    void createFilmWithZeroDuration() {
        Film film = new Film("name", "description", LocalDate.now(), Duration.ofMinutes(0));
        List<String> messages = getValidateErrorMsg(film);
        assertEquals(messages.size(), 1, "Неверное количество сообщений");
        assertTrue(messages.contains(FILM_DURATION_INVALID), "Неверное сообщение об ошибке");
    }

    @Test
    void createFilmWithNegativeDuration() {
        Film film = new Film("name", "description", LocalDate.now(), Duration.ofMinutes(-10));
        List<String> messages = getValidateErrorMsg(film);
        assertEquals(messages.size(), 1, "Неверное количество сообщений");
        assertTrue(messages.contains(FILM_DURATION_INVALID), "Неверное сообщение об ошибке");
    }

    @Test
    void createFilmWithNullDuration() {
        Film film = new Film("name", "description", LocalDate.now(), null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Ошибка валидации при проверке правильных параметров");
        assertDoesNotThrow(() -> filmService.validateResource(film), "Ошибка валидации фильма в контролере");
    }

}
