package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.DIRECTOR_NAME_EMPTY;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Director {

    private int id;
    @NotBlank(message = DIRECTOR_NAME_EMPTY)
    private String name;

}
