package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class InMemoryUserStorage extends AbstractInMemoryStorage<User> implements UserStorage {

}
