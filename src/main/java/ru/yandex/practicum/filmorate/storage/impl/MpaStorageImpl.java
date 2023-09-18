package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class MpaStorageImpl implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mpaRowMapper = new MpaRowMapper();

    @Override
    public Mpa findById(Integer id) {
        String sql = "SELECT * " +
                "FROM ratings " +
                "WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, mpaRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
    }

    @Override
    public List<Mpa> findAll() {
        String sql = "SELECT * " +
                "FROM ratings";
        return jdbcTemplate.query(sql, mpaRowMapper);
    }

}