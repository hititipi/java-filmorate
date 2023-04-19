package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.ResourceService;
import ru.yandex.practicum.filmorate.interfaces.Resource;

import java.util.Collection;

@Slf4j
public abstract class AbstractController<T extends Resource> {
    ResourceService<T> service;

    public T get(int id) {
        return service.get(id);
    }

    public Collection<T> getAll() {
        return service.getAll();
    }

    public T createResource(T resource) {
        return service.createResource(resource);
    }

    public T updateResource(T resource) {
        return service.updateResource(resource);
    }

    public void delete(int id) {
        service.deleteResource(id);
    }


}

