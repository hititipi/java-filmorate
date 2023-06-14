package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.EventType;
import ru.yandex.practicum.filmorate.utils.Operation;

import java.util.List;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewStorage reviewStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FeedStorage feedDbStorage;

    @Override
    public Review getReview(int id) {
        return reviewStorage.get(id);
    }

    @Override
    public Review createReview(Review review) {
        if (!filmStorage.contains(review.getFilmId()) || !userStorage.contains(review.getUserId())) {
            throw new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND);
        }
        Review created = reviewStorage.add(review);
        feedDbStorage.addEvent(
                new Event(
                        created.getUserId(),
                        EventType.REVIEW,
                        Operation.ADD,
                        created.getReviewId()
                )
        );
        return created;
    }

    @Override
    public Review updateReview(Review review) {
        Review updated = reviewStorage.update(review);
        feedDbStorage.addEvent(
                new Event(
                        updated.getUserId(),
                        EventType.REVIEW,
                        Operation.UPDATE,
                        updated.getReviewId()
                )
        );
        return updated;
    }

    @Override
    public void deleteReview(int id) {
        Review review = reviewStorage.get(id);
        feedDbStorage.addEvent(
                new Event(
                        review.getUserId(),
                        EventType.REVIEW,
                        Operation.REMOVE,
                        id
                )
        );
        reviewStorage.delete(id);
    }

    @Override
    public List<Review> getPopularReviewsByFilmId(int filmId, int count) {
        if (filmId == 0) {
            return reviewStorage.getAll(count);
        }
        return reviewStorage.getAll(filmId, count);
    }

    @Override
    public void addLike(int reviewId, int userId) {
        reviewStorage.addLike(reviewId, userId);
    }

    @Override
    public void addDislike(int reviewId, int userId) {
        reviewStorage.addDislike(reviewId, userId);
    }

    @Override
    public void removeLikeOrDislike(int reviewId, int userId) {
        reviewStorage.removeUseful(reviewId, userId);
    }

}