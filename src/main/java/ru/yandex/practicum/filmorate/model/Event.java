package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.utils.EventType;
import ru.yandex.practicum.filmorate.utils.Operation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Instant;

@Data
public class Event {

    private long eventId;
    private long timestamp = Instant.now().toEpochMilli();
    @NotNull
    private final int userId;
    @NotNull
    private final EventType eventType;
    @NotNull
    private final Operation operation;
    @Positive
    private final int entityId;
}
