package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.ResourceService;
import ru.yandex.practicum.filmorate.interfaces.Resource;

import java.util.Collection;

@Slf4j
public abstract class AbstractController<R extends Resource, S extends ResourceService<R, ?>> {
    protected S service;

    public R get(int id) {
        return service.get(id);
    }

    public Collection<R> getAll() {
        return service.getAll();
    }

    public R createResource(R resource) {
        return service.createResource(resource);
    }

    public R updateResource(R resource) {
        return service.updateResource(resource);
    }

    public void delete(int id) {
        service.deleteResource(id);
    }

}

