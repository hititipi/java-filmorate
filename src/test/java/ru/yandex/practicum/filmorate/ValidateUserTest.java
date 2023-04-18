package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.exception.ValidationErrors.*;

public class ValidateUserTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private List<String> getValidateErrorMsg(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        return violations
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    @Test
    void createValidUser() {
        User user = new User("test@mail.ru", "login", "name", LocalDate.now().minusYears(4));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Ошибка валидации при проверке правильных параметров");
    }

    @Test
    void createUserWithNullEmail() {
        User user = new User(null, "login", "name", LocalDate.now().minusYears(4));
        List<String> messages = getValidateErrorMsg(user);
        assertEquals(messages.size(), 2, "Неверное количество сообщений");
        assertTrue(messages.contains(EMAIL_NULL), "Неверное сообщение об ошибке");
        assertTrue(messages.contains(EMAIL_EMPTY), "Неверное сообщение об ошибке");
    }

    @Test
    void createUserWithEmptyEmail() {
        User user = new User("", "login", "name", LocalDate.now().minusYears(4));
        List<String> messages = getValidateErrorMsg(user);
        assertEquals(messages.size(), 1, "Неверное количество сообщений");
        assertTrue(messages.contains(EMAIL_EMPTY), "Неверное сообщение об ошибке");
    }

    @Test
    void createUserWithInvalidEmail() {
        User user = new User("mail.ru", "login", "name", LocalDate.now().minusYears(4));
        List<String> messages = getValidateErrorMsg(user);
        assertEquals(messages.size(), 1, "Неверное количество сообщений");
        assertTrue(messages.contains(EMAIL_INVALID), "Неверное сообщение об ошибке");
    }

    @Test
    void createUserWithNullLogin() {
        User user = new User("test@mail.ru", null, "name", LocalDate.now().minusYears(4));
        List<String> messages = getValidateErrorMsg(user);
        assertEquals(messages.size(), 2, "Неверное количество сообщений");
        assertTrue(messages.contains(LOGIN_NULL), "Неверное сообщение об ошибке");
        assertTrue(messages.contains(LOGIN_EMPTY), "Неверное сообщение об ошибке");
    }

    @Test
    void createUserWithEmptyLogin() {
        User user = new User("test@mail.ru", "", "name", LocalDate.now().minusYears(4));
        List<String> messages = getValidateErrorMsg(user);
        assertEquals(messages.size(), 2, "Неверное количество сообщений");
        assertTrue(messages.contains(LOGIN_EMPTY), "Неверное сообщение об ошибке");
        assertTrue(messages.contains(LOGIN_INVALID), "Неверное сообщение об ошибке");
    }

    @Test
    void createUserWithLoginWithSpace() {
        User user = new User("test@mail.ru", "abc def", "name", LocalDate.now().minusYears(4));
        List<String> messages = getValidateErrorMsg(user);
        assertEquals(messages.size(), 1, "Неверное количество сообщений");
        assertTrue(messages.contains(LOGIN_INVALID), "Неверное сообщение об ошибке");
    }

    @Test
    void createUserWithNullName() {
        User user = new User("test@mail.ru", "login", null, LocalDate.now().minusYears(4));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Ошибка валидации при проверке правильных параметров");
        assertEquals(user.getName(), user.getLogin(), "Имя отличается от логина");
    }

    @Test
    void createUserWithEmptyName() {
        User user = new User("test@mail.ru", "login", "", LocalDate.now().minusYears(4));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Ошибка валидации при проверке правильных параметров");
        assertEquals(user.getName(), user.getLogin(), "Имя отличается от логина");
    }

    @Test
    void createUserWithInvalidBirthday() {
        User user = new User("test@mail.ru", "login", "name", LocalDate.now().plusYears(4));
        List<String> messages = getValidateErrorMsg(user);
        assertEquals(messages.size(), 1, "Неверное количество сообщений");
        assertTrue(messages.contains(BIRTHDAY_INVALID), "Неверное сообщение об ошибке");
    }

}
