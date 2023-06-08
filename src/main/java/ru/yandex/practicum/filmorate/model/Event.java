package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.utils.EventType;
import ru.yandex.practicum.filmorate.utils.Operation;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
public class Event {

    private long eventId;
    private long timestamp;
    @NotNull
    private final int userId;
    @NotNull
    private final EventType eventType;
    @NotNull
    private final Operation operation;
    @NotNull
    private final int entityId;

    public Event(int userId, EventType eventType, Operation operation, int entityId) {
        timestamp = Instant.now().toEpochMilli();
        this.userId = userId;
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
    }
}
