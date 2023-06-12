package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.utils.Messages;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService service;

    @GetMapping("{id}")
    public Review getReview(@PathVariable int id) {
        log.info(Messages.getReview(id));
        return service.getReview(id);
    }

    @GetMapping()
    public List<Review> getAllReviews(@RequestParam(required = false, defaultValue = "0") int filmId,
                                      @RequestParam(required = false, defaultValue = "10") @Positive int count) {
        log.info(Messages.getAllReviews());
        return service.getPopularReviewsByFilmId(filmId, count);
    }

    @PostMapping
    public Review createReview(@Valid @RequestBody Review review) {
        log.info(Messages.createReview());
        return service.createReview(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        log.info(Messages.updateReview(review.getReviewId()));
        return service.updateReview(review);
    }

    @DeleteMapping("{id}")
    public void deleteReview(@PathVariable int id) {
        log.info(Messages.deleteReview(id));
        service.deleteReview(id);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addLike(@PathVariable int reviewId, @PathVariable int userId) {
        log.info(Messages.addReviewLike(reviewId, userId));
        service.addLike(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislike(@PathVariable int reviewId, @PathVariable int userId) {
        log.info(Messages.addReviewDislike(reviewId, userId));
        service.addDislike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void removeLike(@PathVariable int reviewId, @PathVariable int userId) {
        log.info(Messages.removeReviewLike(reviewId, userId));
        service.removeLikeOrDislike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void removeDislike(@PathVariable int reviewId, @PathVariable int userId) {
        log.info(Messages.removeReviewDislike(reviewId, userId));
        service.removeLikeOrDislike(reviewId, userId);
    }

}
