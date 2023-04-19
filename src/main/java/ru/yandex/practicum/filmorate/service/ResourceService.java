package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.interfaces.Resource;
import ru.yandex.practicum.filmorate.interfaces.ResourceStorage;

import java.util.Collection;

public abstract class ResourceService<T extends Resource> {

    public ResourceStorage<T> storage;

    public T get(int id) {
        return storage.get(id);
    }

    public Collection<T> getAll() {
        return storage.getAll();
    }

    boolean containsKey(int id) {
        return storage.contains(id);
    }

    public T createResource(T resource) {
        validateResource(resource);
        storage.add(resource);
        return resource;
    }

    public T updateResource(T resource) {
        validateResource(resource);
        return storage.update(resource);
    }

    public void deleteResource(int id) {
        storage.delete(id);
    }

    public abstract void validateResource(T resource);

}
