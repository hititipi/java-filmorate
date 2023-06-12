package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.utils.EventType;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class EventTypeStorage {

    private final JdbcTemplate jdbcTemplate;

    public EventType findTypeById(int id) {
        String type = jdbcTemplate.queryForObject(
                "SELECT name FROM event_types WHERE id = ?",
                (rs, rowNum) -> rs.getString("name"),
                id
        );
        if (type == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        return EventType.valueOf(type);
    }

    public int findTypeId(EventType type) {
        Integer id = jdbcTemplate.queryForObject(
                "SELECT id FROM event_types WHERE name = ?",
                (rs, rowNum) -> rs.getInt("id"),
                type.name()
        );
        if (id == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        return id;
    }
}
