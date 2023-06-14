package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final DirectorStorage directorStorage;

    @Override
    public Director getDirector(int id) {
        return directorStorage.findById(id);
    }

    @Override
    public List<Director> getAllDirectors() {
        return directorStorage.findAll();
    }

    @Override
    public Director createDirector(Director director) {
        directorStorage.add(director);
        return director;
    }

    @Override
    public void deleteDirector(int id) {
        directorStorage.delete(id);
    }

    @Override
    public void update(Director director) {
        directorStorage.update(director);
    }
}
