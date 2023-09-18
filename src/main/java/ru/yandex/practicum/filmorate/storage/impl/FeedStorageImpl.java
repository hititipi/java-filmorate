package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.EventRowMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FeedStorageImpl implements FeedStorage {

    private final JdbcTemplate jdbcTemplate;
    private final EventRowMapper eventRowMapper = new EventRowMapper();
    private static final String SQL_GET_EVENT_TYPE_ID = "SELECT id FROM event_types WHERE name = ?";
    private static final String SQL_GET_OPERATION_ID = "SELECT id FROM operations WHERE name = ?";

    @Override
    public void addEvent(Event event) {
        Integer eventTypeId = jdbcTemplate.queryForObject(
                SQL_GET_EVENT_TYPE_ID,
                (rs, rowNum) -> rs.getInt("id"),
                event.getEventType().name()
        );
        Integer operationId = jdbcTemplate.queryForObject(
                SQL_GET_OPERATION_ID,
                (rs, rowNum) -> rs.getInt("id"),
                event.getOperation().name()
        );
        String sql = "INSERT into events (timestamp, user_id, event_type_id, operation_id, entity_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                sql,
                event.getTimestamp(),
                event.getUserId(),
                eventTypeId,
                operationId,
                event.getEntityId()
        );
    }

    @Override
    public List<Event> findUserFeed(int userId) {
        String sql = "SELECT events.*, event_types.name, operations.name " +
                "FROM events " +
                "JOIN event_types ON event_types.id=events.event_type_id " +
                "JOIN operations ON operations.id=events.operation_id " +
                "WHERE user_id = ?";
        return jdbcTemplate.query(sql, eventRowMapper, userId);
    }
}
