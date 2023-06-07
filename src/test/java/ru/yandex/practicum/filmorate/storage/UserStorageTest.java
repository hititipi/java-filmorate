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
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;
import static ru.yandex.practicum.filmorate.TestUtils.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserStorageTest {

    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void clearDb() {
        String sql = "DELETE FROM likes";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM friends";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM users";
        jdbcTemplate.update(sql);
    }

    @Test
    void getAll() {
        User user1 = createUser1();
        user1 = userDbStorage.add(user1);
        User user2 = createUser2();
        user2 = userDbStorage.add(user2);
        Collection<User> users = userDbStorage.getAll();
        assertEquals(users.size(), 2);
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void getUser() {
        User user = createUser1();
        user = userDbStorage.add(user);
        User gottenUser = userDbStorage.get(user.getId());
        assertEquals(user, gottenUser);
    }

    @Test
    void getFilmWithInvalidId() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userDbStorage.get(-1));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(exception.getMessage(), RESOURCE_NOT_FOUND);
    }

    @Test
    void addUser() {
        User user = createUser1();
        user = userDbStorage.add(user);
        Collection<User> users = userDbStorage.getAll();
        assertEquals(users.size(), 1);
        assertTrue(users.contains(user));
    }

    @Test
    void updateUser() {
        User user = createUser1();
        user = userDbStorage.add(user);
        User updatedUser = createUser2();
        updatedUser.setId(user.getId());
        updatedUser = userDbStorage.update(updatedUser);
        User gottenUser = userDbStorage.get(user.getId());
        assertEquals(updatedUser, gottenUser);
    }

    @Test
    void updateNotExistingFilm() {
        User user = createUser1();
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userDbStorage.update(user));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(exception.getMessage(), RESOURCE_NOT_FOUND);
    }

    @Test
    void deleteUser() {
        User user1 = createUser1();
        user1 = userDbStorage.add(user1);
        userDbStorage.delete(user1.getId());
        Collection<User> users = userDbStorage.getAll();
        Assertions.assertTrue(users.isEmpty());
    }

    @Test
    void checkContains() {
        User user1 = createUser1();
        user1 = userDbStorage.add(user1);
        int id = user1.getId();
        assertDoesNotThrow(() -> userDbStorage.checkContains(id));
    }

    @Test
    void checkContainsInvalidId() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userDbStorage.checkContains(-1));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(exception.getMessage(), RESOURCE_NOT_FOUND);
    }
}
