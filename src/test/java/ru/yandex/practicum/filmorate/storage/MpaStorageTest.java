package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;
import static ru.yandex.practicum.filmorate.TestUtils.ratings;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MpaStorageTest {

    private final MpaStorage mpaStorage;

    @Test
    void getAllTest() {
        List<Mpa> allGenres = mpaStorage.findAll();
        assertEquals(allGenres.size(), 5);
        for (Mpa mpa : ratings) {
            assertTrue(allGenres.contains(mpa));
        }
    }

    @Test
    void getMpa() {
        Mpa mpa = mpaStorage.findById(1);
        assertEquals(mpa, ratings[0]);
    }

    @Test
    void getMpaByInvalidId() {
        ValidationException exception = assertThrows(ValidationException.class, () -> mpaStorage.findById(-1));
        assertEquals(exception.getMessage(), RESOURCE_NOT_FOUND);
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

}
