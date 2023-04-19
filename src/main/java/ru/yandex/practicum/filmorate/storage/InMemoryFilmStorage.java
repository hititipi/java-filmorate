package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class InMemoryFilmStorage extends AbstractInMemoryStorage<Film> implements FilmStorage {

    @Override
    public List<Film> getMostPopular(Integer count) {
        return resources.values().stream()
                .sorted(Comparator.comparing(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

}
