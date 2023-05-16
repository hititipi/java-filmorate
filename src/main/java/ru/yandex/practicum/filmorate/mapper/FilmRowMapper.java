package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt(1);
        String name = rs.getString(2);
        String description = rs.getString(3);
        LocalDate releaseDate = rs.getDate(4).toLocalDate();
        int duration = rs.getInt(5);
        Mpa mpa = new Mpa(rs.getInt(6), rs.getString(7));
        Film film = new Film(name, description, releaseDate, duration);
        film.setId(id);
        film.setMpa(mpa);
        return film;
    }
}
