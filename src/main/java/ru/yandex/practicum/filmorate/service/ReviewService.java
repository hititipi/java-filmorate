package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FeedDbStorage;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.utils.EventType;
import ru.yandex.practicum.filmorate.utils.Operation;

import java.util.List;

import static ru.yandex.practicum.filmorate.exception.ValidationErrors.RESOURCE_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final FeedDbStorage feedDbStorage;

    public Review get(int id) {
        return reviewStorage.get(id);
    }

    public Review createResource(Review review) {
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

    public Review updateResource(Review review) {
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

    public void deleteResource(int id) {
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

    public List<Review> getPopularReviewsByFilmId(int filmId, int count) {
        if (filmId == 0) {
            return reviewStorage.getAll(count);
        }
        return reviewStorage.getAll(filmId, count);
    }

    public void addLike(int reviewId, int userId) {
        reviewStorage.addLike(reviewId, userId);
    }

    public void addDislike(int reviewId, int userId) {
        reviewStorage.addDislike(reviewId, userId);
    }

    public void removeLikeOrDislike(int reviewId, int userId) {
        reviewStorage.removeUseful(reviewId, userId);
    }

}