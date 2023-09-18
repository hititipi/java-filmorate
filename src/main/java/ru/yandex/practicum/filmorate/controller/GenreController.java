package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.utils.Messages;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable Integer id) {
        log.info(Messages.getGenre(id));
        return genreService.getGenre(id);
    }

    @GetMapping
    public List<Genre> getAllGenres() {
        log.info(Messages.getAllGenres());
        return genreService.getAllGenres();
    }

}