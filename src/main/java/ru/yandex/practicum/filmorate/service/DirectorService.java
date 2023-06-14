package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

public interface DirectorService {

    Director getDirector(int id);

    Collection<Director> getAllDirectors();

    Director createDirector(Director director);

    void update(Director director);

    void deleteDirector(int id);
}
