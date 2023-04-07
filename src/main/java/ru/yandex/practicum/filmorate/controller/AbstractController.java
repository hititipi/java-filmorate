package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.utils.Messages;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.interfaces.Resource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@Slf4j
public abstract class AbstractController<T extends Resource> {

    final Map<Integer, T> resources = new HashMap<>();
    private int idCounter = 1;

    public Collection<T> getAll() {
        return resources.values();
    }

    boolean containsResource(T resource) {
        return resources.containsKey(resource.getId());
    }

    public T createResource(T resource) {
        validateResource(resource);
        if (containsResource(resource)) {
            log.warn(Messages.addAlreadyExistsResource(resource));
            throw new ValidationException(RESOURCE_ALREADY_EXISTS);
        }
        resource.setId(idCounter++);
        resources.put(resource.getId(), resource);
        log.info(Messages.resourceAdded(resource));
        return resource;
    }

    public T updateResource(T resource) {
        validateResource(resource);
        if (containsResource(resource)) {
            resources.put(resource.getId(), resource);
            log.info(Messages.resourceUpdated(resource));
        } else {
            log.warn(Messages.updateNotExistingResource(resource));
            throw new ValidationException(RESOURCE_NOT_FOUND);
        }
        return resource;
    }

    abstract void validateResource(T resource);

}

