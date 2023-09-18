package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.*;

@Data
public class Film {

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
    private Set<Director> directors = new HashSet<>();

}
