package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.FilmGenreMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

@Component
@RequiredArgsConstructor
public class FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreMapper filmGenreMapper;
    private final GenreMapper genreMapper;

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
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO film_genres (film_id, genre_id) " +
                "VALUES");
        for (Genre genre : film.getGenres()) {
            builder.append("(");
            builder.append(film.getId());
            builder.append(",");
            builder.append(genre.getId());
            builder.append("),");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(";");
        jdbcTemplate.update(builder.toString());
    }

    public Film loadFilmGenre(Film film) {
        String sql = "SELECT genre_id, genres.name " +
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
        String sql = "SELECT film_genres.*, genres.name " +
                "FROM film_genres " +
                "JOIN genres ON genres.id = film_genres.genre_id " +
                "WHERE film_id IN  (" + idsStr + ");";
        List<FilmGenre> allFilmGenres = jdbcTemplate.query(sql, filmGenreMapper);
        Map<Integer, Set<Genre>> genresByFilmId = allFilmGenres.stream()
                .collect(Collectors.groupingBy(FilmGenre::getFilmId, mapping(FilmGenre::getGenre, toSet())));
        for (Film film : films) {
            Set<Genre> filmGenres = genresByFilmId.get(film.getId());
            if (filmGenres != null) {
                film.setGenres(filmGenres);
            }
        }
        return films;
    }

}