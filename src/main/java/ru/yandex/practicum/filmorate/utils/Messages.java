package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.interfaces.Resource;

import java.time.LocalDate;

public class Messages {

    public static String tryAddResource(Resource resource) {
        return String.format("Запрос на создание ресурса: %s", resource.getResourceName());
    }

    public static String addAlreadyExistsResource(Resource resource) {
        return String.format("Попытка создания уже существующего ресурса: %s, id =  %d",
                resource.getResourceName(), resource.getId());
    }

    public static String resourceAdded(Resource resource) {
        return String.format("Добавлен ресурс: %s, id =  %d",
                resource.getResourceName(), resource.getId());
    }

    public static String tryUpdateResource(Resource resource) {
        return String.format("Запрос на обновление ресурса: %s, id =  %d",
                resource.getResourceName(), resource.getId());
    }

    public static String resourceUpdated(Resource resource) {
        return String.format("Ресурс обновлен: %s, id =  %d",
                resource.getResourceName(), resource.getId());
    }

    public static String updateNotExistingResource(Resource resource) {
        return String.format("Попытка обновления несуществующего ресурса: %s, id =  %d",
                resource.getResourceName(), resource.getId());
    }

    public static String getAllFilms(int size) {
        return String.format("Передана коллекция фильмов: %d шт.", size);
    }

    public static String getAllUsers(int size) {
        return String.format("Передана коллекция пользователей: %d шт.", size);
    }

    public static String invalidReleaseDate(LocalDate date) {
        return String.format("Некорректная дата релиза: %s", date);
    }

}
