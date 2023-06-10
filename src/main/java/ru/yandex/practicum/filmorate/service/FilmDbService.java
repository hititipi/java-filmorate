package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.utils.Messages;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmDbService extends ResourceService<Film, FilmDbStorage> {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final DirectorStorage directorStorage;

    @Autowired
    public FilmDbService(FilmDbStorage storage, DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
        this.storage = storage;
    }

    private void validateReleaseDate(LocalDate releaseDate) {
        if (releaseDate != null && MIN_RELEASE_DATE.isAfter(releaseDate)) {
            log.info(Messages.invalidReleaseDate(releaseDate));
            throw new ValidationException(HttpStatus.BAD_REQUEST, RESOURCE_NOT_FOUND);
        }
    }

    @Override
    public void validateResource(Film film) {
        validateReleaseDate(film.getReleaseDate());
    }

    @Override
    public Film get(int id) {
        Film film = super.get(id);
        film = storage.loadFilmGenre(film);
        film = directorStorage.loadFilmDirectors(film);
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        Collection<Film> films = super.getAll();
        List<Film> filmLIst = new ArrayList<>(films);
        filmLIst = storage.loadFilmGenres(filmLIst);
        filmLIst = directorStorage.loadFilmDirectors(filmLIst);
        return filmLIst;
    }

    @Override
    public Film createResource(Film film) {
        film = super.createResource(film);
        storage.setFilmGenre(film);
        directorStorage.setFilmDirectors(film);
        return film;
    }

    @Override
    public Film updateResource(Film resource) {
        Film film = super.updateResource(resource);
        storage.updateFilmGenre(film);
        directorStorage.updateFilmDirectors(film);
        return film;
    }

    @Override
    public void deleteResource(int id) {
        super.deleteResource(id);
        storage.deleteFilmGenre(id);
        directorStorage.deleteFilmDirectors(id);
    }

    public List<Film> getDirectorFilmsWithSort(int directorId, String sortBy) {
        if (directorStorage.findById(directorId) != null) {
            List<Film> films = storage.findDirectorFilmsWithSort(directorId, sortBy);
            films = storage.loadFilmGenres(films);
            films = directorStorage.loadFilmDirectors(films);
            return films;
        } else throw new ValidationException(HttpStatus.BAD_REQUEST, RESOURCE_NOT_FOUND);
    }
}