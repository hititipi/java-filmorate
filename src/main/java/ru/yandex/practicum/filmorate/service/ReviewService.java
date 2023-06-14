package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewService {

    Review getReview(int id);

    Review createReview(Review review);

    Review updateReview(Review review);

    void deleteReview(int id);

    List<Review> getPopularReviewsByFilmId(int filmId, int count);

    void addLike(int reviewId, int userId);

    void addDislike(int reviewId, int userId);

    void removeLikeOrDislike(int reviewId, int userId);

}
