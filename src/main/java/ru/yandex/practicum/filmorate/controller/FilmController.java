package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;
import ru.yandex.practicum.filmorate.utils.Messages;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final LikeService likeService;

    @GetMapping("{id}")
    public Film getFilm(@PathVariable int id) {
        log.info(Messages.getFilm(id));
        return filmService.getFilm(id);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info(Messages.getAllFilms());
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info(Messages.addFilm());
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info(Messages.updateFilm(film.getId()));
        return filmService.updateFilm(film);
    }

    @DeleteMapping("{id}")
    public void deleteFilm(@PathVariable int id) {
        log.info(Messages.deleteFilm(id));
        filmService.deleteFilm(id);
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
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count,
                                      @RequestParam(required = false, defaultValue = "0") @Positive int genreId,
                                      @RequestParam(required = false, defaultValue = "0") @Positive int year) {
        log.info(Messages.getPopularFilms(count, genreId, year));
        return likeService.getMostLikedFilms(count, genreId, year);
    }

    @GetMapping("/director/{directorId}")
    public Collection<Film> getSortedFilms(
            @PathVariable("directorId") Integer directorId, @RequestParam String sortBy) {
        log.info(Messages.getSortedFilms(sortBy));
        return filmService.getDirectorFilmsWithSort(directorId, sortBy);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam int userId, @RequestParam int friendId) {
        log.info(Messages.getCommonsFilms(userId, friendId));
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("search")
    public List<Film> getFilmsBySearch(@RequestParam(required = false) String query,
                                       @RequestParam(required = false) String by) {
        log.info(Messages.getFilmBySubstring());
        return (query == null && by == null) ? likeService.getAllFilmsSortedByRating() : filmService.searchFilms(query, by);
    }

}