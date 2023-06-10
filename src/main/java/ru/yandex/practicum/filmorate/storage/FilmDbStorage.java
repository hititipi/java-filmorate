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
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final FilmRowMapper filmRowMapper = new FilmRowMapper();
    private final GenreMapper genreMapper = new GenreMapper();
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
        String sql = "SELECT films.*, ratings.* " +
                "FROM films " +
                "JOIN ratings on ratings.id = films.rating_id";
        return jdbcTemplate.query(sql, filmRowMapper);
    }

    @Override
    public Film get(int id) {
        String sql = "SELECT films.*, ratings.* " +
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

    public void deleteFilmGenre(int id) {
        String sql = "DELETE FROM film_genres " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateFilmGenre(Film film) {
        deleteFilmGenre(film.getId());
        setFilmGenre(film);
    }

    public void setFilmGenre(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        String sql = "INSERT INTO film_genres (film_id, genre_id) " +
                "VALUES(?,?)";
        List<Genre> genres = new ArrayList<>(film.getGenres());
        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, genres.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return film.getGenres().size();
                    }
                });
    }

    public Film loadFilmGenre(Film film) {
        String sql = "SELECT genre_id, genres.* " +
                "FROM film_genres " +
                "JOIN genres ON genres.id = film_genres.genre_id " +
                "WHERE film_id IN  ( ? );";
        List<Genre> genres = jdbcTemplate.query(sql, genreMapper, film.getId());
        film.setGenres(new HashSet<>(genres));
        return film;
    }

    public List<Film> loadFilmGenres(List<Film> films) {
        if (films.isEmpty()) {
            return Collections.emptyList();
        }
        String idsStr = films.stream()
                .map(Film::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String sql = "SELECT film_genres.*, genres.* " +
                "FROM film_genres " +
                "JOIN genres ON genres.id = film_genres.genre_id " +
                "WHERE film_id IN  (" + idsStr + ");";

        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        Map<Integer, Set<Genre>> genresByFilmId = mapList.stream()
                .collect(Collectors.groupingBy(k -> (Integer) k.get("film_id"),
                        mapping(k -> new Genre((Integer) k.get("genre_id"), (String) k.get("name")), toSet())));

        for (Film film : films) {
            Set<Genre> filmGenres = genresByFilmId.get(film.getId());
            if (filmGenres != null) {
                film.setGenres(filmGenres);
            }
        }
        return films;
    }

    public List<Film> findDirectorFilmsWithSort(int directorId, String sortBy) {
        String sql;
        if (sortBy.equals("year")) {
            sql = "SELECT f.*, r.* " +
                    "FROM film_directors fd " +
                    "JOIN films f ON f.id = fd.film_id " +
                    "JOIN ratings r ON f.rating_id = r.id " +
                    "WHERE director_id = ? " +
                    "ORDER BY year(f.release_date)";

        } else if (sortBy.equals("likes")) {
            sql = "SELECT f.*, r.*," +
                    "(SELECT count(*) FROM likes WHERE fd.film_id = likes.film_id) AS likes " +
                    "FROM film_directors fd " +
                    "JOIN films f ON f.id = fd.film_id " +
                    "JOIN ratings r ON f.rating_id = r.id " +
                    "WHERE director_id = ? " +
                    "ORDER BY likes DESC";

        } else {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }

        return jdbcTemplate.query(
                sql,
                new FilmRowMapper(),
                directorId
        );
    }
}
