package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.interfaces.Resource;

import java.time.LocalDate;

public class Messages {

    public static String tryAddResource(Resource resource) {
        return String.format("Запрос на создание ресурса: %s", resource.getResourceName());
    }

    public static String tryUpdateResource(Resource resource) {
        return String.format("Запрос на обновление ресурса: %s, id = %d",
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

    public static String getAllGenres() {
        return "Запрос на получение всех жанров.";
    }

    public static String getGenre(int id) {
        return String.format("Запрос на получение жанра: id = %d", id);
    }

    public static String getAllRatings() {
        return "Запрос на получение всех рейтингов.";
    }

    public static String getRating(int id) {
        return String.format("Запрос на получение рейтинга: id = %d", id);
    }

    public static String invalidReleaseDate(LocalDate date) {
        return String.format("Некорректная дата релиза: %s", date);
    }

    public static String getAllReviews() {
        return "Запрос на получение всех отзывов.";
    }

    public static String getReview(int id) {
        return String.format("Запрос на получение отзыва: id = %d", id);
    }

    public static String createReview() {
        return "Запрос на создание отзыва.";
    }

    public static String updateReview(int id) {
        return String.format("Запрос на обновление отзыва: id = %d", id);
    }

    public static String deleteReview(int id) {
        return String.format("Запрос на удаление отзыва: id = %d", id);
    }

    public static String addReviewLike(int reviewId, int userId) {
        return String.format("Запрос на добавление лайка на отзыв review_id = %d, user_id = %d", reviewId, userId);
    }

    public static String addReviewDislike(int reviewId, int userId) {
        return String.format("Запрос на добавление дислайка на отзыв review_id = %d, user_id = %d", reviewId, userId);
    }

    public static String removeReviewLike(int reviewId, int userId) {
        return String.format("Запрос на удаление лайка на отзыв review_id = %d, user_id = %d", reviewId, userId);
    }

    public static String removeReviewDislike(int reviewId, int userId) {
        return String.format("Запрос на удаление дислайка на отзыв review_id = %d, user_id = %d", reviewId, userId);
    }

    public static String getFeed(int id) {
        return String.format("Запрос на получение ленты событий: userId = %d", id);
    }
}
