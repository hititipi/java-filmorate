package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.DirectorRowMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import static ru.yandex.practicum.filmorate.exception.ValidationErrors.DIRECTOR_NAME_EMPTY;
import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class DirectorStorage {
    private final JdbcTemplate jdbcTemplate;
    private final DirectorRowMapper directorRowMapper = new DirectorRowMapper();

    public List<Director> findAll() {
        String sql = "SELECT * " +
                "FROM directors " +
                "ORDER BY id";
        return jdbcTemplate.query(sql, directorRowMapper);
    }

    public Director findById(int id) {
        String sql = "SELECT * " +
                "FROM directors " +
                "WHERE id=?";
        try {
            return jdbcTemplate.queryForObject(sql, directorRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
    }

    public Director add(Director director) {
        if (director.getName().trim().isEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, DIRECTOR_NAME_EMPTY);
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO directors (name) VALUES (?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[]{"id"});
            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            director.setId(keyHolder.getKey().intValue());
        }
        return director;
    }

    public void delete(int id) {
        String sql = "DELETE FROM directors WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void update(Director director) {
        String sql = "UPDATE directors SET name = ? WHERE id = ?";
        int count = jdbcTemplate.update(sql, director.getName(), director.getId());
        if (count != 1) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
    }

    public void deleteFilmDirectors(int id) {
        String sql = "DELETE FROM film_directors " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateFilmDirectors(Film film) {
        deleteFilmDirectors(film.getId());
        setFilmDirectors(film);
    }

    public void setFilmDirectors(Film film) {
        if (film.getDirectors() == null || film.getDirectors().isEmpty()) {
            return;
        }
        String sql = "INSERT INTO film_directors (film_id, director_id) VALUES(?,?)";
        List<Director> directors = new ArrayList<>(film.getDirectors());
        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, directors.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return film.getDirectors().size();
                    }
                });
    }

    public Film loadFilmDirectors(Film film) {
        String sql = "SELECT director_id, directors.* " +
                "FROM film_directors " +
                "JOIN directors ON directors.id = film_directors.director_id " +
                "WHERE film_id IN  ( ? );";
        List<Director> directors = jdbcTemplate.query(sql, directorRowMapper, film.getId());
        film.setDirectors(new HashSet<>(directors));
        return film;
    }

    public List<Film> loadFilmDirectors(List<Film> films) {
        if (films.isEmpty()) {
            return Collections.emptyList();
        }
        String idsStr = films.stream()
                .map(Film::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String sql = "SELECT film_directors.*, directors.* " +
                "FROM film_directors " +
                "JOIN directors ON directors.id = film_directors.director_id " +
                "WHERE film_id IN  (" + idsStr + ");";

        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        Map<Integer, Set<Director>> directorsByFilmId = mapList.stream()
                .collect(Collectors.groupingBy(k -> (Integer) k.get("film_id"),
                        mapping(k -> new Director((Integer) k.get("id"), (String) k.get("name")), toSet())));

        for (Film film : films) {
            Set<Director> filmDirectors = directorsByFilmId.get(film.getId());
            if (filmDirectors != null) {
                film.setDirectors(filmDirectors);
            }
        }
        return films;
    }
}
