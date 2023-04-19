package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.interfaces.Resource;

import java.time.LocalDate;

public class Messages {

    public static String tryAddResource(Resource resource) {
        return String.format("Запрос на создание ресурса: %s", resource.getResourceName());
    }

    public static String addAlreadyExistsResource(Resource resource) {
        return String.format("Попытка создания уже существующего ресурса: %s, id = %d",
                resource.getResourceName(), resource.getId());
    }

    public static String resourceAdded(Resource resource) {
        return String.format("Добавлен ресурс: %s, id =  %d",
                resource.getResourceName(), resource.getId());
    }

    public static String tryUpdateResource(Resource resource) {
        return String.format("Запрос на обновление ресурса: %s, id = %d",
                resource.getResourceName(), resource.getId());
    }

    public static String resourceUpdated(Resource resource) {
        return String.format("Ресурс обновлен: %s, id =  %d",
                resource.getResourceName(), resource.getId());
    }

    public static String resourceDeleted(int id) {
        return String.format("Ресурс удален: id =  %d", id);
    }

    public static String deleteNotExistingResource(int id) {
        return String.format("Попытка удаления несуществующего ресурса: id = %d", id);
    }

    public static String updateNotExistingResource(Resource resource) {
        return String.format("Попытка обновления несуществующего ресурса: %s, id = %d",
                resource.getResourceName(), resource.getId());
    }

    public static String getAllFilms() {
        return "Запрос на получение всех фильмов.";
    }

    public static String getFilm(int id) {
        return String.format("Запрос на получение фильма: id = %d", id);
    }

    public static String addLike(int filmId, int userId) {
        return String.format("Запрос на добавление лайка: film_id = %d, user_id = %d", filmId, userId);
    }

    public static String deleteLike(int filmId, int userId) {
        return String.format("Запрос на удаление лайка: film_id = %d, user_id = %d", filmId, userId);
    }

    public static String getPopularFilms(int count) {
        return String.format("Запрос на получение списка популярных фильмов: количество = %d", count);
    }

    public static String getAllUsers() {
        return "Запрос на получение всех пользователей.";
    }

    public static String getUser(int id) {
        return String.format("Запрос на получение пользователя: id = %d", id);
    }

    public static String addFriend(int id, int friendId) {
        return String.format("Запрос на добавление в друзья: id1 = %d, id2 = %d", id, friendId);
    }

    public static String deleteFriend(int id, int friendId) {
        return String.format("Запрос на удаление из друзей: id1 = %d, id2 = %d", id, friendId);
    }

    public static String getFriends(int id) {
        return String.format("Запрос на получение списка друзей: id = %d", id);
    }

    public static String getCommonFriends(int id, int friendId) {
        return String.format("Запрос на получение общих друзей: id1 = %d, id2 = %d", id, friendId);
    }

    public static String invalidReleaseDate(LocalDate date) {
        return String.format("Некорректная дата релиза: %s", date);
    }

    public static String filmNotFound(int id) {
        return String.format("Фильм не найден: id = %d", id);
    }

    public static String userNotFound(int id) {
        return String.format("Пользователь не найден: id = %d", id);
    }

    public static String likeAlreadySet(int filmId, int userId) {
        return String.format("Лайк уже стоит: film_id = %d, user_id = %d", filmId, userId);
    }

    public static String likeNotSet(int filmId, int userId) {
        return String.format("Лайк не поставлен: film_id = %d, user_id = %d", filmId, userId);
    }

    public static String usersAlreadyFriends(int id, int friendId) {
        return String.format("Пользователи уже друзья: id1 = %d, id2 = %d", id, friendId);
    }

    public static String usersNotFriends(int id, int friendId) {
        return String.format("Пользователи не друзья: id1 = %d, id2 = %d", id, friendId);
    }
}
