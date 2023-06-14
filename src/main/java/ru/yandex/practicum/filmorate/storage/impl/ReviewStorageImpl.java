package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.ReviewRowMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.util.List;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@Component
@AllArgsConstructor
public class ReviewStorageImpl implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;
    private final ReviewRowMapper reviewRowMapper = new ReviewRowMapper();

    @Override
    public List<Review> getAll(int count) {
        String sql = "SELECT reviews.*, COALESCE(SUM(review_likes.useful), 0) AS useful " +
                "FROM  reviews " +
                "LEFT JOIN review_likes ON reviews.id = review_likes.review_id " +
                "GROUP BY reviews.ID " +
                "ORDER BY useful DESC " +
                "LIMIT ? ";
        return jdbcTemplate.query(sql, reviewRowMapper, count);
    }

    @Override
    public List<Review> getAll(int filmId, int count) {
        String sql = "SELECT reviews.*, SUM(review_likes.useful) AS useful " +
                "FROM  reviews " +
                "LEFT JOIN review_likes ON reviews.id = review_likes.review_id " +
                "GROUP BY reviews.ID " +
                "HAVING film_id = ? " +
                "ORDER BY useful DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, reviewRowMapper, filmId, count);
    }

    @Override
    public Review get(int id) {
        String sql = "SELECT reviews.*, COALESCE(SUM(review_likes.useful), 0) AS useful " +
                "FROM  reviews " +
                "LEFT JOIN review_likes ON reviews.id = review_likes.review_id " +
                "GROUP BY reviews.id " +
                "HAVING reviews.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, reviewRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
    }

    @Override
    public Review add(Review review) {
        String sql = "INSERT INTO reviews (content, is_positive, user_id, film_id) " +
                "VALUES (?,?,?,?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"ID"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setInt(3, review.getUserId());
            stmt.setInt(4, review.getFilmId());
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            review.setReviewId(keyHolder.getKey().intValue());
        }
        return review;
    }

    @Override
    public Review update(Review review) {
        String sql = "UPDATE reviews " +
                "SET (content, is_positive) = (?,?) " +
                "WHERE id= ?";
        int count = jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(),
                review.getReviewId());
        if (count != 1) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        return get(review.getReviewId());
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM reviews " +
                "WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void addLike(int reviewId, int userId) {
        String sql = "MERGE INTO review_likes (review_id, user_id, useful) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, reviewId, userId, 1);
    }

    @Override
    public void addDislike(int reviewId, int userId) {
        String sql = "MERGE INTO review_likes (review_id, user_id, useful) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, reviewId, userId, -1);
    }

    @Override
    public void removeUseful(int reviewId, int userId) {
        String sql = "DELETE FROM review_likes " +
                "WHERE review_id = ? " +
                "AND user_id = ?";
        jdbcTemplate.update(sql, reviewId, userId);
    }

}
