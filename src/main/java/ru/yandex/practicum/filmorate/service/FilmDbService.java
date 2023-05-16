package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.utils.Messages;

import java.time.LocalDate;
import java.util.*;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.*;

@Slf4j
@Service
public class FilmDbService extends ResourceService<Film, FilmDbStorage> {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private final FilmGenreStorage filmGenreStorage;

    @Autowired
    public FilmDbService(FilmDbStorage storage, FilmGenreStorage filmGenreStorage) {
        this.storage = storage;
        this.filmGenreStorage = filmGenreStorage;
    }

    private void validateReleaseDate(LocalDate releaseDate) {
        if (releaseDate != null && MIN_RELEASE_DATE.isAfter(releaseDate)) {
            log.info(Messages.invalidReleaseDate(releaseDate));
            throw new ValidationException(HttpStatus.BAD_REQUEST, FILM_RELEASE_INVALID);
        }
    }

    @Override
    public void validateResource(Film film) {
        validateReleaseDate(film.getReleaseDate());
    }

    @Override
    public Film get(int id) {
        Film film = super.get(id);
        film = filmGenreStorage.loadFilmGenre(film);
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        Collection<Film> films = super.getAll();
        List<Film> filmLIst = new ArrayList<>(films);
        return filmGenreStorage.loadFilmGenres(filmLIst);
    }

    @Override
    public Film createResource(Film film) {
        film = super.createResource(film);
        filmGenreStorage.setFilmGenre(film);
        return film;
    }

    @Override
    public Film updateResource(Film resource) {
        Film film = super.updateResource(resource);
        filmGenreStorage.updateFilmGenre(film);
        return film;
    }

    @Override
    public void deleteResource(int id) {
        super.deleteResource(id);
        filmGenreStorage.deleteFilmGenre(id);
    }
}
