package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("films.id");
        String name = rs.getString("films.name");
        String description = rs.getString("films.description");
        LocalDate releaseDate = rs.getDate("films.release_date").toLocalDate();
        int duration = rs.getInt("films.duration");
        Mpa mpa = new Mpa(rs.getInt("films.rating_id"), rs.getString("ratings.name"));
        Film film = new Film(name, description, releaseDate, duration);
        film.setId(id);
        film.setMpa(mpa);
        return film;
    }
}
