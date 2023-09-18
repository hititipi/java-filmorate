package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    Review get(int id);

    Review add(Review review);

    Review update(Review review);

    void delete(int id);

    List<Review> getAll(int count);

    List<Review> getAll(int filmId, int count);

    void addLike(int reviewId, int userId);

    void addDislike(int reviewId, int userId);

    void removeUseful(int reviewId, int userId);

}
