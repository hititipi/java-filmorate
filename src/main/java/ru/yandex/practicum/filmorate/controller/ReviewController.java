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
    public Review get(@PathVariable int id) {
        log.info(Messages.getReview(id));
        return service.get(id);
    }

    @GetMapping()
    public List<Review> getAll(@RequestParam(required = false, defaultValue = "0") int filmId,
                               @RequestParam(required = false, defaultValue = "10") @Positive int count) {
        log.info(Messages.getAllReviews());
        return service.getPopularReviewsByFilmId(filmId, count);
    }

    @PostMapping
    public Review createResource(@Valid @RequestBody Review review) {
        log.info(Messages.createReview());
        return service.createResource(review);
    }

    @PutMapping
    public Review updateResource(@Valid @RequestBody Review review) {
        log.info(Messages.updateReview(review.getReviewId()));
        return service.updateResource(review);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable int id) {
        log.info(Messages.deleteReview(id));
        service.deleteResource(id);
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
