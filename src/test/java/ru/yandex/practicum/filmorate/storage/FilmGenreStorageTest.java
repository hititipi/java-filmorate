package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.TestUtils.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmGenreStorageTest {

    private final FilmGenreStorage filmGenreStorage;
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
    void setGenre() {
        Set<Genre> filmGenres = Set.of(genres[0], genres[1]);
        Film film = createFilm1();
        film.setGenres(filmGenres);
        film = filmDbStorage.add(film);
        filmGenreStorage.setFilmGenre(film);
        Film gettingFilm = filmGenreStorage.loadFilmGenre(film);
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
        filmGenreStorage.setFilmGenre(film1);
        Set<Genre> filmGenres2 = Set.of(genres[3], genres[4]);
        Film film2 = createFilm2();
        film2.setGenres(filmGenres2);
        film2 = filmDbStorage.add(film2);
        filmGenreStorage.setFilmGenre(film2);

        List<Film> films = List.of(film1, film2);
        films = filmGenreStorage.loadFilmGenres(films);
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
        films = filmGenreStorage.loadFilmGenres(films);
        assertTrue(films.isEmpty());
    }

    @Test
    void deleteGenres() {
        Set<Genre> filmGenres = Set.of(genres[0], genres[1]);
        Film film = createFilm1();
        film.setGenres(filmGenres);
        film = filmDbStorage.add(film);
        filmGenreStorage.setFilmGenre(film);
        filmGenreStorage.deleteFilmGenre(film.getId());

        Film gettingFilm = filmGenreStorage.loadFilmGenre(film);
        Set<Genre> gettingGenres = gettingFilm.getGenres();

        assertTrue(gettingGenres.isEmpty());
    }

    @Test
    void updateGenre() {
        Set<Genre> filmGenres = Set.of(genres[0], genres[1]);
        Film film = createFilm1();
        film.setGenres(filmGenres);
        film = filmDbStorage.add(film);
        filmGenreStorage.setFilmGenre(film);
        Set<Genre> filmGenres2 = Set.of(genres[3], genres[4]);
        film.setGenres(filmGenres2);
        filmGenreStorage.updateFilmGenre(film);

        Film gettingFilm = filmGenreStorage.loadFilmGenre(film);
        Set<Genre> gottenGenres = gettingFilm.getGenres();

        assertEquals(gottenGenres.size(), filmGenres2.size());
        assertTrue(gottenGenres.containsAll(filmGenres2));
        assertTrue(filmGenres2.containsAll(gottenGenres));
    }

}
