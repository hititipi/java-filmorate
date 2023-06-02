package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.interfaces.Resource;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Mpa implements Resource {

    private int id;
    private String name;

    @Override
    public String getResourceName() {
        return "Рейтинг";
    }
}
