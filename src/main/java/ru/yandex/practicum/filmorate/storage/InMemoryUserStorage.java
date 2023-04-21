package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage extends AbstractInMemoryStorage<User> implements UserStorage {

    @Override
    public Collection<User> getUserFriends(int id) {
        Set<Integer> friends = resources.get(id).getFriends();
        return friends.stream().map(this::get).collect(Collectors.toList());
    }

    @Override
    public Collection<User> getCommonFriends(int id, int otherId) {
        return getUserFriends(id).stream()
                .filter(getUserFriends(otherId)::contains)
                .collect(Collectors.toList());
    }

}
