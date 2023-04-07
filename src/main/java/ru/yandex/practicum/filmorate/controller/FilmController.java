package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.utils.Messages;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends AbstractController<Film> {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    @GetMapping
    public Collection<Film> getAll() {
        log.info(Messages.getAllFilms(resources.size()));
        return super.getAll();
    }

    @Override
    @PostMapping
    public Film createResource(@Valid @RequestBody Film film) {
        log.info(Messages.tryAddResource(film));
        return super.createResource(film);
    }

    @Override
    @PutMapping
    public Film updateResource(@Valid @RequestBody Film film) {
        log.info(Messages.tryUpdateResource(film));
        return super.updateResource(film);
    }

    private void validateReleaseDate(LocalDate releaseDate) {
        if (releaseDate != null && MIN_RELEASE_DATE.isAfter(releaseDate)) {
            log.info(Messages.invalidReleaseDate(releaseDate));
            throw new ValidationException(FILM_RELEASE_INVALID);
        }
    }

    @Override
    public void validateResource(Film film) {
        validateReleaseDate(film.getReleaseDate());
    }

}
