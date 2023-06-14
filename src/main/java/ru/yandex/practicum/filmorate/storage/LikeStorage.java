package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class LikeStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper = new FilmRowMapper();

    public void addLike(Integer filmID, Integer userID) {
        String sql = "MERGE INTO likes (film_id, user_id) " +
                "VALUES (?,?)";
        jdbcTemplate.update(sql, filmID, userID);
    }

    public void deleteLike(Integer filmID, Integer userID) {
        String sql = "DELETE FROM likes " +
                "WHERE film_id = ? AND user_id = ?";
        int count = jdbcTemplate.update(sql, filmID, userID);
        if (count == 0) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
    }

    public List<Film> getMostLikedFilms(Integer count) {
        String sql = "SELECT films.*, ratings.name " +
                "FROM films " +
                "LEFT JOIN likes ON films.id=likes.film_id " +
                "JOIN ratings ON ratings.id = films.rating_id " +
                "GROUP BY films.id " +
                "ORDER BY COUNT(likes.user_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, filmRowMapper, count);
    }

    public List<Film> getPopularByGenre(int count, int genreId) {
        String sql = "SELECT films.*, ratings.name " +
                "FROM films " +
                "LEFT JOIN likes ON films.id=likes.film_id " +
                "JOIN ratings ON ratings.id = films.rating_id " +
                "JOIN film_genres ON film_genres.film_id = films.id " +
                "WHERE film_genres.genre_id = ? " +
                "GROUP BY films.id " +
                "ORDER BY COUNT(likes.user_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, filmRowMapper, genreId, count);
    }

    public List<Film> getPopularByYear(int count, int year) {
        String sql = "SELECT films.*, ratings.name " +
                "FROM films " +
                "LEFT JOIN likes ON films.id=likes.film_id " +
                "JOIN ratings ON ratings.id = films.rating_id " +
                "WHERE EXTRACT(year FROM CAST(films.release_date AS date)) = ? " +
                "GROUP BY films.id " +
                "ORDER BY COUNT(likes.user_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, filmRowMapper, year, count);
    }

    public List<Film> getPopularByGenreAndYear(int count, int genreId, int year) {
        String sql = "SELECT films.*, ratings.name " +
                "FROM films " +
                "LEFT JOIN likes ON films.id=likes.film_id " +
                "JOIN ratings ON ratings.id = films.rating_id " +
                "JOIN film_genres ON film_genres.film_id = films.id " +
                "WHERE film_genres.genre_id = ? " +
                "AND EXTRACT(year FROM CAST(films.release_date AS date)) = ? " +
                "GROUP BY films.id " +
                "ORDER BY COUNT(likes.user_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, filmRowMapper, genreId, year, count);
    }

    public List<Film> getAllFilmsSortedByRating() {
        String sql = "SELECT films.*, ratings.name " +
                "FROM films " +
                "LEFT JOIN likes ON films.id=likes.film_id " +
                "JOIN ratings ON ratings.id = films.rating_id " +
                "GROUP BY films.id " +
                "ORDER BY COUNT(likes.user_id) DESC ";
        return jdbcTemplate.query(sql, filmRowMapper);
    }

    public Map<Integer, List<Integer>> getSameLikesByUser(int userId) {
        String sqlQuery = "SELECT DISTINCT * FROM likes AS l1 " +
                "LEFT JOIN likes AS l2 ON l1.user_id = l2.user_id " +
                "JOIN likes AS l3 ON l2.film_id = l3.film_id " +
                "WHERE l3.user_id = ?";
        Map<Integer, List<Integer>> likes = new HashMap<>();
        jdbcTemplate.query(sqlQuery, rs -> {
            int id = rs.getInt("user_id");
            int filmId = rs.getInt("film_id");
            likes.putIfAbsent(id, new ArrayList<>());
            likes.get(id).add(filmId);
        }, userId);
        return likes;
    }
}
