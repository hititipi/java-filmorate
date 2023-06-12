package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
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
public class FriendStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private final FriendStorage friendStorage;
    private final UserDbStorage userDbStorage;

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
    void addFriend() {
        User user1 = createUser1();
        user1 = userDbStorage.addUser(user1);
        User user2 = createUser2();
        user2 = userDbStorage.addUser(user2);
        friendStorage.addFriend(user1.getId(), user2.getId());
        Collection<User> friends = friendStorage.getFriends(user1.getId());
        assertEquals(friends.size(), 1);
        assertTrue(friends.contains(user2));
    }

    @Test
    void deleteFriend() {
        User user1 = createUser1();
        user1 = userDbStorage.addUser(user1);
        User user2 = createUser2();
        user2 = userDbStorage.addUser(user2);
        friendStorage.addFriend(user1.getId(), user2.getId());
        friendStorage.deleteFriend(user1.getId(), user2.getId());
        Collection<User> friends = friendStorage.getFriends(user1.getId());
        assertTrue(friends.isEmpty());
    }

    @Test
    void deleteFriedForNotExistingUser() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> friendStorage.deleteFriend(-2, -1));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(exception.getMessage(), RESOURCE_NOT_FOUND);
    }

    @Test
    void getFriend() {
        User user1 = createUser1();
        user1 = userDbStorage.addUser(user1);
        User user2 = createUser2();
        user2 = userDbStorage.addUser(user2);
        User user3 = createUser3();
        user3 = userDbStorage.addUser(user3);
        friendStorage.addFriend(user1.getId(), user2.getId());
        friendStorage.addFriend(user1.getId(), user3.getId());
        Collection<User> friends = friendStorage.getFriends(user1.getId());
        assertEquals(friends.size(), 2);
        assertTrue(friends.contains(user2));
        assertTrue(friends.contains(user3));
    }

    @Test
    void getCommonFriends() {
        User user1 = createUser1();
        user1 = userDbStorage.addUser(user1);
        User user2 = createUser2();
        user2 = userDbStorage.addUser(user2);
        User user3 = createUser3();
        user3 = userDbStorage.addUser(user3);
        friendStorage.addFriend(user1.getId(), user3.getId());
        friendStorage.addFriend(user2.getId(), user3.getId());
        Collection<User> friends = friendStorage.getCommonFriends(user1.getId(), user2.getId());
        assertEquals(friends.size(), 1);
        assertTrue(friends.contains(user3));
    }

}
