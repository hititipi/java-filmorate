package ru.yandex.practicum.filmorate.exception;

public class ValidationErrors {

    public static final String RESOURCE_ALREADY_EXISTS = "Уже существует ресурс с таким id";
    public static final String RESOURCE_NOT_FOUND = "Ресурс не найден";

    // user errors
    public static final String EMAIL_NULL = "Email не может быть null";
    public static final String EMAIL_EMPTY = "Email не может быть пустым";
    public static final String EMAIL_INVALID = "Неверный формат email";
    public static final String LOGIN_NULL = "Логин не может быть null";
    public static final String LOGIN_EMPTY = "Логин не может быть пустым";
    public static final String LOGIN_INVALID = "Логин не должен содержать пробелы";
    public static final String BIRTHDAY_INVALID = "Неверно указан день рождения";
    public static final String USERS_ALREADY_FRIENDS = "Пользователи уже друзья";
    public static final String USERS_NOT_FRIENDS = "Пользователи не друзья";

    // film errors
    public static final String FILM_NAME_NULL = "Название фильма не может быть null";
    public static final String FILM_NAME_EMPTY = "Название фильма не может быть пустым";
    public static final String FILM_DESCRIPTION_INVALID = "Длина описания не может превышать 200 символов";
    public static final String FILM_RELEASE_INVALID = "Некорректная дата релиза";
    public static final String FILM_DURATION_INVALID = "Некорректная продолжительность фильма";
    public static final String LIKE_ALREADY_SET = "Лайк уже поставлен";
    public static final String LIKE_NOT_SET = "Лайк не поставлен";

    // directors errors
    public static final String DIRECTOR_NAME_EMPTY = "Имя режиссёра не может быть пустым";

}
