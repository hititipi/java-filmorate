package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.LikeService;
import ru.yandex.practicum.filmorate.utils.Messages;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends AbstractController<Film, FilmDbService> {

    private final LikeService likeService;

    @Autowired
    public FilmController(FilmDbService service, LikeService likeService) {
        this.service = service;
        this.likeService = likeService;
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.info(Messages.getAllFilms());
        return super.getAll();
    }

    @GetMapping("{id}")
    public Film get(@PathVariable int id) {
        log.info(Messages.getFilm(id));
        return super.get(id);
    }

    @PostMapping
    public Film createResource(@Valid @RequestBody Film film) {
        log.info(Messages.tryAddResource(film));
        return super.createResource(film);
    }

    @PutMapping
    public Film updateResource(@Valid @RequestBody Film film) {
        log.info(Messages.tryUpdateResource(film));
        return super.updateResource(film);
    }

    @PutMapping("{filmId}/like/{userId}")
    public void addLike(@PathVariable int filmId, @PathVariable int userId) {
        log.info(Messages.addLike(filmId, userId));
        likeService.addLike(filmId, userId);
    }

    @DeleteMapping("{filmId}/like/{userId}")
    public void deleteLike(@PathVariable int filmId, @PathVariable int userId) {
        log.info(Messages.deleteLike(filmId, userId));
        likeService.deleteLike(filmId, userId);
    }

    @GetMapping("popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info(Messages.getPopularFilms(count));
        return likeService.getMostLikedFilms(count);
    }

    @GetMapping("search")
    public List<Film> getFilmsBySearch(@RequestParam(required = false) String query,
                                       @RequestParam(required = false) String by) {
        log.info(Messages.getFilmBySubstring());
        return (query == null && by == null) ? likeService.getAllFilmsSortedByRating() : service.searchFilms(query, by);
    }

    @GetMapping("/director/{directorId}")
    public Collection<Film> getSortedFilms(
            @PathVariable("directorId") Integer directorId, @RequestParam String sortBy) {
        log.info(Messages.getSortedFilms(sortBy));
        return service.getDirectorFilmsWithSort(directorId, sortBy);
    }
}