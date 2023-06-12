package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.utils.Operation;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class OperationStorage {

    private final JdbcTemplate jdbcTemplate;

    public Operation findOperationById(int id) {
        String operation = jdbcTemplate.queryForObject(
                "SELECT name FROM operations WHERE id = ?",
                (rs, rowNum) -> rs.getString("name"),
                id
        );
        if (operation == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        return Operation.valueOf(operation);
    }

    public int findOperationId(Operation operation) {
        Integer id = jdbcTemplate.queryForObject(
                "SELECT id FROM operations WHERE name = ?",
                (rs, rowNum) -> rs.getInt("id"),
                operation.name()
        );
        if (id == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        return id;
    }
}
