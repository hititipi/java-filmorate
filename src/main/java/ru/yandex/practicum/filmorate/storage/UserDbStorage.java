package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final UserRowMapper userRowMapper = new UserRowMapper();
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean contains(int id) {
        String sql = "SELECT count(*) " +
                "FROM users " +
                "WHERE id = ?";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return result != null && result == 1;
    }

    @Override
    public void checkContains(int id) {
        if (!contains(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
    }

    @Override
    public Collection<User> getAll() {
        String query = "SELECT * " +
                "FROM users;";
        return jdbcTemplate.query(query, userRowMapper);
    }

    @Override
    public User get(int id) {
        String sql = "SELECT * " +
                "FROM users " +
                "WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
    }

    @Override
    public User add(User user) {
        String sql = "INSERT INTO users(login,name, email, birthday) " +
                "VALUES(?,?,?,?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"ID"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            user.setId(keyHolder.getKey().intValue());
        }
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users " +
                "SET (login,name, email, birthday) = (?,?,?,?) " +
                "WHERE id=?";
        int count = jdbcTemplate.update(sql, user.getLogin(), user.getName(), user.getEmail(),
                user.getBirthday(), user.getId());
        if (count != 1) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        return user;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users " +
                "WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
