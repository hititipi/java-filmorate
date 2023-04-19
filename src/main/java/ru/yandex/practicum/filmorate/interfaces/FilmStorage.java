package ru.yandex.practicum.filmorate.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends ResourceStorage<Film> {

    List<Film> getMostPopular(Integer count);
}
