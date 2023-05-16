package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.interfaces.Resource;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class Genre implements Resource {
    private int id;
    private String name;

    @Override
    public String getResourceName() {
        return "жанр";
    }
}

