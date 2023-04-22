package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.interfaces.Resource;
import ru.yandex.practicum.filmorate.interfaces.ResourceStorage;

import java.util.Collection;

@Slf4j
public abstract class ResourceService<R extends Resource, S extends ResourceStorage<R>> {

    protected S storage;

    public R get(int id) {
        return storage.get(id);
    }

    public Collection<R> getAll() {
        return storage.getAll();
    }

    public R createResource(R resource) {
        validateResource(resource);
        storage.add(resource);
        return resource;
    }

    public R updateResource(R resource) {
        validateResource(resource);
        return storage.update(resource);
    }

    public void deleteResource(int id) {
        storage.delete(id);
    }

    public abstract void validateResource(R resource);

}
