package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;

import java.util.List;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class LikeStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper = new FilmRowMapper();

    public void addLike(Integer filmID, Integer userID) {
        String sql = "INSERT INTO likes (film_id, user_id) " +
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

    public List<Film> getAllFilmsSortedByRating() {
        String sql = "SELECT films.*, ratings.name " +
                "FROM films " +
                "LEFT JOIN likes ON films.id=likes.film_id " +
                "JOIN ratings ON ratings.id = films.rating_id " +
                "GROUP BY films.id " +
                "ORDER BY COUNT(likes.user_id) DESC ";
        return jdbcTemplate.query(sql, filmRowMapper);
    }

}
