package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage extends AbstractInMemoryStorage<Film> implements FilmStorage {

}
