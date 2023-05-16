package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.yandex.practicum.filmorate.interfaces.Resource;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.*;

@Data
public class Film implements Resource {

    private int id;
    @NotNull(message = FILM_NAME_NULL)
    @NotEmpty(message = FILM_NAME_EMPTY)
    private final String name;
    @Size(max = 200, message = FILM_DESCRIPTION_INVALID)
    private final String description;
    private final LocalDate releaseDate;
    @Positive(message = FILM_DURATION_INVALID)
    private final int duration;
    @NotNull
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();
    @JsonIgnore
    private Set<Integer> likes = new HashSet<>();

    @Override
    public String getResourceName() {
        return "фильм";
    }

    public void addLike(int userId) {
        likes.add(userId);
    }

    public void deleteLike(int userId) {
        likes.remove(userId);
    }

    public boolean containsLike(int userId) {
        return likes.contains(userId);
    }
}
