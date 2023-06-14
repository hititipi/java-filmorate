package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface LikeStorage {

    void addLike(int filmID, int userID);

    void deleteLike(int filmID, int userID);

    List<Film> getPopularByGenreAndYear(int count, int genreId, int year);

    List<Film> getPopularByGenre(int count, int genreId);

    List<Film> getPopularByYear(int count, int year);

    List<Film> getMostLikedFilms(int count);

    List<Film> getAllFilmsSortedByRating();

    Map<Integer, List<Integer>> getSameLikesByUser(int userId);
}
