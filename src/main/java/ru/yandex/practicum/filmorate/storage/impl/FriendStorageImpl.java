package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class FriendStorageImpl implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper = new UserRowMapper();

    @Override
    public Collection<User> getFriends(int id) {
        String sql = "SELECT * " +
                "FROM users " +
                "WHERE id IN (SELECT friend_id FROM friends WHERE user_id=?)";
        return jdbcTemplate.query(sql, userRowMapper, id);
    }

    @Override
    public void addFriend(int id, int friendId) {
        String sql = "INSERT INTO friends(user_id, friend_id) " +
                "VALUES(?,?)";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        String sql = "DELETE friends " +
                "WHERE user_id = ? AND friend_id = ?";
        int count = jdbcTemplate.update(sql, id, friendId);
        if (count != 1) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
    }

    @Override
    public List<User> getCommonFriends(int user1ID, int user2ID) {
        String sql = "SELECT users.* " +
                "FROM users " +
                "JOIN friends AS f1 on(users.id = f1.friend_id AND f1.user_id = ?) " +
                "JOIN friends AS f2 on (users.id = f2.friend_id AND f2.user_id =?)";
        return jdbcTemplate.query(sql, userRowMapper, user1ID, user2ID);
    }

}
