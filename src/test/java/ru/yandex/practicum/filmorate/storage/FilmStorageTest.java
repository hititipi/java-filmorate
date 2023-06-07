package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;
import static ru.yandex.practicum.filmorate.TestUtils.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmStorageTest {

    private final FilmDbStorage filmDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void clearDb() {
        String sql = "DELETE FROM film_genres";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM films";
        jdbcTemplate.update(sql);
    }

    @Test
    void getAllFilms() {
        Film film1 = createFilm1();
        Film film2 = createFilm2();
        filmDbStorage.add(film1);
        filmDbStorage.add(film2);
        Collection<Film> films = filmDbStorage.getAll();
        Assertions.assertEquals(films.size(), 2);
        Assertions.assertTrue(films.contains(film1));
        Assertions.assertTrue(films.contains(film1));
    }

    @Test
    void getFilm() {
        Film film = createFilm1();
        film = filmDbStorage.add(film);
        Film gottenFilm = filmDbStorage.get(film.getId());
        assertEquals(film, gottenFilm);
    }

    @Test
    void getFilmWithInvalidId() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmDbStorage.get(-1));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(exception.getMessage(), RESOURCE_NOT_FOUND);
    }

    @Test
    void addFilm() {
        Film film = createFilm1();
        film = filmDbStorage.add(film);
        Collection<Film> films = filmDbStorage.getAll();
        assertEquals(films.size(), 1);
        assertTrue(films.contains(film));
    }

    @Test
    void updateFilm() {
        Film film = createFilm1();
        film = filmDbStorage.add(film);
        Film updateFilm = createFilm2();
        updateFilm.setId(film.getId());
        updateFilm = filmDbStorage.update(updateFilm);
        Film gottenFilm = filmDbStorage.get(film.getId());
        assertEquals(updateFilm, gottenFilm);
    }

    @Test
    void updateNotExistingFilm() {
        Film film = createFilm1();
        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmDbStorage.update(film));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(exception.getMessage(), RESOURCE_NOT_FOUND);
    }

    @Test
    void deleteFilm() {
        Film film1 = createFilm1();
        film1 = filmDbStorage.add(film1);
        filmDbStorage.delete(film1.getId());
        Collection<Film> films = filmDbStorage.getAll();
        Assertions.assertTrue(films.isEmpty());
    }

    @Test
    void checkContains() {
        Film film1 = createFilm1();
        film1 = filmDbStorage.add(film1);
        int id = film1.getId();
        assertDoesNotThrow(() -> filmDbStorage.checkContains(id));
    }

    @Test
    void checkContainsInvalidId() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmDbStorage.checkContains(-1));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(exception.getMessage(), RESOURCE_NOT_FOUND);
    }

    @Test
    void setGenre() {
        Set<Genre> filmGenres = Set.of(genres[0], genres[1]);
        Film film = createFilm1();
        film.setGenres(filmGenres);
        film = filmDbStorage.add(film);
        filmDbStorage.setFilmGenre(film);
        Film gettingFilm = filmDbStorage.loadFilmGenre(film);
        Set<Genre> gettingGenres = gettingFilm.getGenres();
        assertEquals(gettingGenres.size(), filmGenres.size());
        assertTrue(gettingGenres.containsAll(filmGenres));
        assertTrue(filmGenres.containsAll(gettingGenres));
    }

    @Test
    void loadGenres() {
        Set<Genre> filmGenres1 = Set.of(genres[0], genres[1]);
        Film film1 = createFilm1();
        film1.setGenres(filmGenres1);
        film1 = filmDbStorage.add(film1);
        filmDbStorage.setFilmGenre(film1);
        Set<Genre> filmGenres2 = Set.of(genres[3], genres[4]);
        Film film2 = createFilm2();
        film2.setGenres(filmGenres2);
        film2 = filmDbStorage.add(film2);
        filmDbStorage.setFilmGenre(film2);

        List<Film> films = List.of(film1, film2);
        films = filmDbStorage.loadFilmGenres(films);
        Set<Genre> gottenGenres1 = films.get(0).getGenres();

        assertEquals(gottenGenres1.size(), filmGenres1.size());
        assertTrue(gottenGenres1.containsAll(filmGenres1));
        assertTrue(filmGenres1.containsAll(gottenGenres1));

        Set<Genre> gottenGenres2 = films.get(1).getGenres();
        assertEquals(gottenGenres2.size(), filmGenres2.size());
        assertTrue(gottenGenres2.containsAll(filmGenres2));
        assertTrue(filmGenres2.containsAll(gottenGenres2));
    }

    @Test
    void loadEmptyGenres() {
        List<Film> films = Collections.emptyList();
        films = filmDbStorage.loadFilmGenres(films);
        assertTrue(films.isEmpty());
    }

    @Test
    void deleteGenres() {
        Set<Genre> filmGenres = Set.of(genres[0], genres[1]);
        Film film = createFilm1();
        film.setGenres(filmGenres);
        film = filmDbStorage.add(film);
        filmDbStorage.setFilmGenre(film);
        filmDbStorage.deleteFilmGenre(film.getId());

        Film gettingFilm = filmDbStorage.loadFilmGenre(film);
        Set<Genre> gettingGenres = gettingFilm.getGenres();

        assertTrue(gettingGenres.isEmpty());
    }

    @Test
    void updateGenre() {
        Set<Genre> filmGenres = Set.of(genres[0], genres[1]);
        Film film = createFilm1();
        film.setGenres(filmGenres);
        film = filmDbStorage.add(film);
        filmDbStorage.setFilmGenre(film);
        Set<Genre> filmGenres2 = Set.of(genres[3], genres[4]);
        film.setGenres(filmGenres2);
        filmDbStorage.updateFilmGenre(film);

        Film gettingFilm = filmDbStorage.loadFilmGenre(film);
        Set<Genre> gottenGenres = gettingFilm.getGenres();

        assertEquals(gottenGenres.size(), filmGenres2.size());
        assertTrue(gottenGenres.containsAll(filmGenres2));
        assertTrue(filmGenres2.containsAll(gottenGenres));
    }

}
