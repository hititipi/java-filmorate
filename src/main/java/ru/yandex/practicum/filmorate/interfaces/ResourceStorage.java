package ru.yandex.practicum.filmorate.interfaces;

import java.util.Collection;

public interface ResourceStorage<T extends Resource> {

    boolean contains(int id);

    Collection<T> getAll();

    T get(int id);

    T add(T resource);

    T update(T resource);

    void delete(int id);
}
