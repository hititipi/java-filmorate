package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.utils.Messages;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping("{id}")
    public Director getDirector(@PathVariable int id) {
        log.info(Messages.getDirector(id));
        return directorService.getDirector(id);
    }

    @GetMapping
    public Collection<Director> getAllDirectors() {
        log.info(Messages.getAllDirectors());
        return directorService.getAllDirectors();
    }

    @PostMapping
    public Director createDirector(@Valid @RequestBody Director director) {
        log.info(Messages.addDirector());
        return directorService.createDirector(director);
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        log.info(Messages.updateDirector(director.getId()));
        directorService.update(director);
        return director;
    }

    @DeleteMapping("{id}")
    public void deleteDirector(@PathVariable int id) {
        log.info(Messages.deleteDirector(id));
        directorService.deleteDirector(id);
    }
}
