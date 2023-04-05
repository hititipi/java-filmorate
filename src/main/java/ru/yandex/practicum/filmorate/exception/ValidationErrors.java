package ru.yandex.practicum.filmorate.exception;

public class ValidationErrors {

    // user errors
    public static final String EMAIL_NULL = "Email не может быть null";
    public static final String EMAIL_EMPTY = "Email не может быть пустым";
    public static final String EMAIL_INVALID = "Неверный формат email";
    public static final String LOGIN_NULL = "Логин не может быть null";
    public static final String LOGIN_EMPTY = "Логин не может быть пустым";
    public static final String LOGIN_INVALID = "Логин не должен содержать пробелы";
    public static final String BIRTHDAY_INVALID = "Неверно указан день рождения";
    public static final String USER_ALREADY_EXISTS = "Уже существует пользователь с таким id";
    public static final String USER_NOT_FOUND = "Пользователь не найден";

    // film errors
    public static final String FILM_NAME_NULL = "Название фильма не может быть null";
    public static final String FILM_NAME_EMPTY = "Название фильма не может быть пустым";
    public static final String FILM_DESCRIPTION_INVALID = "Длина описания не может превышать 200 символов";
    public static final String FILM_RELEASE_INVALID = "Некорректная дата релиза";
    public static final String FILM_DURATION_INVALID = "Некорректная продолжительность фильма";
    public static final String FILM_ALREADY_EXISTS = "Уже существует фильм с таким id";
    public static final String FILM_NOT_FOUND = "Фильм не найден";

}
