package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;
import static ru.yandex.practicum.filmorate.TestUtils.genres;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GenreStorageTest {

    private final GenreStorage genreStorage;

    @Test
    void getAllTest() {
        List<Genre> allGenres = genreStorage.findAll();
        assertEquals(allGenres.size(), 6);
        for (Genre genre : genres) {
            assertTrue(allGenres.contains(genre));
        }
    }

    @Test
    void getGenre() {
        Genre genre = genreStorage.findById(1);
        assertEquals(genre, genres[0]);
    }

    @Test
    void getGenreByInvalidId() {
        ValidationException exception = assertThrows(ValidationException.class, () -> genreStorage.findById(-1));
        assertEquals(exception.getMessage(), RESOURCE_NOT_FOUND);
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

}
