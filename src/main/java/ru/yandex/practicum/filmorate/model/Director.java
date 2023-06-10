package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.interfaces.Resource;

import javax.validation.constraints.NotBlank;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.DIRECTOR_NAME_EMPTY;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Director implements Resource {
    private int id;
    @NotBlank(message = DIRECTOR_NAME_EMPTY)
    private String name;

    @Override
    public String getResourceName() {
        return "режиссёр";
    }
}
