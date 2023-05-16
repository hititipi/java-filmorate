package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt(1);
        String login = rs.getString(2);
        String name = rs.getString(3);
        String email = rs.getString(4);
        LocalDate birthday = rs.getDate(5).toLocalDate();
        User user = new User(login, name, email, birthday);
        user.setId(id);
        return user;
    }
}
