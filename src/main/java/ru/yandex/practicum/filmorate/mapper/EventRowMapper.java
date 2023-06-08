package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventTypeStorage;
import ru.yandex.practicum.filmorate.storage.OperationStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@Component
public class EventRowMapper implements RowMapper<Event> {

    private final EventTypeStorage eventTypeStorage;
    private final OperationStorage operationStorage;

    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("event_id");
        Event event = new Event(
                rs.getInt("user_id"),
                eventTypeStorage.findTypeById(rs.getInt("event_type_id")),
                operationStorage.findOperationById(rs.getInt("operation_id")),
                rs.getInt("entity_id")
        );
        event.setTimestamp(rs.getLong("timestamp"));
        event.setEventId(id);
        return event;
    }
}
