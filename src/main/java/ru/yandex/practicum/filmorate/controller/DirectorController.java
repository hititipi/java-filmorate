package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.utils.Messages;

import java.util.Collection;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping("{id}")
    public Director get(@PathVariable int id) {
        log.info(Messages.getDirector(id));
        return directorService.getDirectorById(id);
    }

    @GetMapping
    public Collection<Director> getAll() {
        log.info(Messages.getAllDirectors());
        return directorService.getAll();
    }

    @PostMapping
    public Director createResource(@RequestBody Director director) {
        log.info(Messages.tryAddResource(director));
        return directorService.createResource(director);
    }

    @PutMapping
    public Director updateResource(@RequestBody Director director) {
        log.info(Messages.tryUpdateResource(director));
        directorService.update(director);
        return director;
    }

    @DeleteMapping("{id}")
    public void deleteResource(@PathVariable int id) {
        log.info(Messages.resourceDeleted(id));
        directorService.delete(id);
    }
}
