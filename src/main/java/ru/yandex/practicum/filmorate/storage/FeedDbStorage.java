package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.EventRowMapper;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FeedDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private final EventTypeStorage eventTypeStorage;
    private final OperationStorage operationStorage;
    private final EventRowMapper eventRowMapper;

    public void addEvent(Event event) {
        int eventTypeId = eventTypeStorage.findTypeId(event.getEventType());
        int operationId = operationStorage.findOperationId(event.getOperation());
        String sql = "insert into events (timestamp, user_id, event_type_id, operation_id, entity_id) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                sql,
                event.getTimestamp(),
                event.getUserId(),
                eventTypeId,
                operationId,
                event.getEntityId()
        );
    }

    public List<Event> findUserFeed(int userId) {
        String sql = "select * from events where user_id = ?";
        return jdbcTemplate.query(sql, eventRowMapper, userId);
    }
}
