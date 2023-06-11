package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public Director getDirectorById(int id) {
        return directorStorage.findById(id);
    }

    public List<Director> getAll() {
        return directorStorage.findAll();
    }

    public Director createResource(Director director) {
        directorStorage.add(director);
        return director;
    }

    public void delete(int id) {
        directorStorage.delete(id);
    }

    public void update(Director director) {
        directorStorage.update(director);
    }
}
