package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.interfaces.Resource;
import ru.yandex.practicum.filmorate.interfaces.ResourceStorage;
import ru.yandex.practicum.filmorate.utils.Messages;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@Slf4j
public class AbstractInMemoryStorage<T extends Resource> implements ResourceStorage<T> {

    final protected Map<Integer, T> resources = new HashMap<>();
    private int idCounter = 1;

    boolean contains(T resource) {
        return contains(resource.getId());
    }

    @Override
    public boolean contains(int id) {
        return resources.containsKey(id);
    }

    @Override
    public Collection<T> getAll() {
        return resources.values();
    }

    @Override
    public T get(int id) {
        if (contains(id)) {
            return resources.get(id);
        } else {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
    }

    @Override
    public T add(T resource) {
        if (contains(resource)) {
            log.warn(Messages.addAlreadyExistsResource(resource));
            throw new ValidationException(HttpStatus.BAD_REQUEST, RESOURCE_ALREADY_EXISTS);
        }
        resource.setId(idCounter++);
        resources.put(resource.getId(), resource);
        log.info(Messages.resourceAdded(resource));
        return resource;
    }

    @Override
    public T update(T resource) {
        if (contains(resource)) {
            resources.put(resource.getId(), resource);
            log.info(Messages.resourceUpdated(resource));
            return resource;
        } else {
            log.warn(Messages.updateNotExistingResource(resource));
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
    }

    @Override
    public void delete(int id) {
        if (!contains(id)) {
            log.warn(Messages.deleteNotExistingResource(id));
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        } else {
            resources.remove(id);
            log.info(Messages.resourceDeleted(id));
        }
    }

}
