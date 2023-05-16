package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final FilmRowMapper filmRowMapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean contains(int id) {
        String sql = "SELECT count(*) FROM films WHERE id = ?";
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
    public Collection<Film> getAll() {
        String sql = "SELECT films.*, ratings.name " +
                "FROM films " +
                "JOIN ratings on ratings.id = films.rating_id;";
        return jdbcTemplate.query(sql, filmRowMapper);
    }

    @Override
    public Film get(int id) {
        String sql = "SELECT films.*, ratings.name " +
                "FROM films " +
                "JOIN ratings ON ratings.id = films.rating_id " +
                "WHERE films.id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, filmRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
    }

    @Override
    public Film add(Film film) {
        String sql = "INSERT INTO FILMS (name, description, release_date, duration, rating_id) " +
                "VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"ID"});
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            LocalDate releaseDate = film.getReleaseDate();
            preparedStatement.setDate(3, Date.valueOf(releaseDate));
            preparedStatement.setInt(4, film.getDuration());
            preparedStatement.setInt(5, film.getMpa().getId());
            return preparedStatement;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            film.setId(keyHolder.getKey().intValue());
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films " +
                "SET name = ?,description = ?,release_date = ?,duration = ?,rating_id = ? " +
                "WHERE id = ?";
        int count = jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (count != 1) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        return film;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM films " +
                "WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
