package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.interfaces.Resource;

import javax.validation.constraints.*;
import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.*;

@Data
public class User implements Resource {

    private int id;
    @NotNull(message = EMAIL_NULL)
    @NotBlank(message = EMAIL_EMPTY)
    @Email(message = EMAIL_INVALID)
    private final String email;
    @NotNull(message = LOGIN_NULL)
    @NotBlank(message = LOGIN_EMPTY)
    @Pattern(regexp = "\\S+", message = LOGIN_INVALID)
    private final String login;
    private final String name;
    @PastOrPresent(message = BIRTHDAY_INVALID)
    private final LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = (name == null || name.isEmpty()) ? login : name;
        this.birthday = birthday;
    }

    @Override
    public String getResourceName() {
        return "пользователь";
    }
}
