package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewRowMapper implements RowMapper<Review> {

    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Review(rs.getInt("reviews.id"),
                rs.getString("reviews.content"),
                rs.getBoolean("reviews.is_positive"),
                rs.getInt("reviews.user_id"),
                rs.getInt("reviews.film_id"),
                rs.getInt("useful"));
    }
}
