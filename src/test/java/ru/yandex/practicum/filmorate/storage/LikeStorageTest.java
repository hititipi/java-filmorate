package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;
import static ru.yandex.practicum.filmorate.TestUtils.*;

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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FilmStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.LikeStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorageImpl;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LikeStorageTest {

    private final LikeStorageImpl likeStorage;
    private final UserDbStorageImpl userDbStorage;
    private final FilmStorageImpl filmDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void clearDb() {
        String sql = "DELETE FROM likes";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM friends";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM film_genres";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM films";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM users";
        jdbcTemplate.update(sql);
    }

    private List<Integer> getLikedUsers(int filmId) {
        String sql = "SELECT user_id " +
                "FROM likes " +
                "WHERE film_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, filmId);
    }

    @Test
    void addLikeTest() {
        User user = createUser1();
        user = userDbStorage.addUser(user);
        Film film = createFilm1();
        film = filmDbStorage.createFilm(film);
        likeStorage.addLike(film.getId(), user.getId());
        List<Integer> usersIDs = getLikedUsers(film.getId());
        assertEquals(usersIDs.size(), 1);
        assertTrue(usersIDs.contains(user.getId()));
    }

    @Test
    void deleteLikeTest() {
        User user = createUser1();
        user = userDbStorage.addUser(user);
        Film film = createFilm1();
        film = filmDbStorage.createFilm(film);
        likeStorage.addLike(film.getId(), user.getId());
        likeStorage.deleteLike(film.getId(), user.getId());
        List<Integer> usersIDs = getLikedUsers(film.getId());
        assertTrue(usersIDs.isEmpty());
    }

    @Test
    void deleteLikeWithNotExistingUser() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> likeStorage.deleteLike(-2, -1));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(exception.getMessage(), RESOURCE_NOT_FOUND);
    }

    @Test
    void getMostLikedFilms() {
        User user1 = createUser1();
        user1 = userDbStorage.addUser(user1);
        User user2 = createUser2();
        user2 = userDbStorage.addUser(user2);
        User user3 = createUser3();
        user3 = userDbStorage.addUser(user3);
        Film film1 = createFilm1();
        film1 = filmDbStorage.createFilm(film1);
        Film film2 = createFilm2();
        filmDbStorage.createFilm(film2);
        Film film3 = createFilm3();
        film3 = filmDbStorage.createFilm(film3);

        likeStorage.addLike(film3.getId(), user1.getId());
        likeStorage.addLike(film3.getId(), user2.getId());
        likeStorage.addLike(film3.getId(), user3.getId());
        likeStorage.addLike(film1.getId(), user1.getId());
        likeStorage.addLike(film1.getId(), user2.getId());

        List<Film> popularFilms = likeStorage.getMostLikedFilms(3);

        assertEquals(popularFilms.size(), 3);
        assertEquals(popularFilms.get(0), film3);
        assertEquals(popularFilms.get(1), film1);
    }

}