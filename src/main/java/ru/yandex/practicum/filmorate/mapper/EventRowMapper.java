package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.utils.EventType;
import ru.yandex.practicum.filmorate.utils.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventRowMapper implements RowMapper<Event> {

    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("events.event_id");
        Event event = new Event(
                rs.getInt("events.user_id"),
                EventType.valueOf(rs.getString("event_types.name")),
                Operation.valueOf(rs.getString("operations.name")),
                rs.getInt("events.entity_id")
        );
        event.setTimestamp(rs.getLong("events.timestamp"));
        event.setEventId(id);
        return event;
    }
}
