package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.AlreadyExistException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, String> usersEmails = new HashMap<>();
    private Integer id = 1;

    private Integer generateId() {
        return this.id++;
    }

    @Override
    public List<User> findAll() {
        List<User> usersList = new ArrayList<>();
        users.values().forEach(user -> usersList.add(user));
        return usersList;
    }

    @Override
    public User save(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        usersEmails.put(user.getId(), user.getEmail());
        return user;
    }

    @Override
    public User update(User user, Integer userId) {
        log.info("Получен запрос на обновление пользовтаеля с id = {}", userId);

        User curUser = getUserById(userId);

        getUserAndCheckDuplicateEmailWhenUpdateUser(curUser, user);

        if (user.getName() != null) {
            curUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            curUser.setEmail(user.getEmail());
        }

        usersEmails.put(userId, user.getEmail());

        log.info("Пользователь с id = {} успешно обновлен", userId);

        return curUser;
    }

    @Override
    public User getUserById(Integer id) {
        User user = findAll().stream().filter(u -> u.getId().intValue() == id.intValue()).findFirst().orElse(null);
        return user;
    }

    private void checkDuplicateEmailWhenCreateUser(String email) {
        if (usersEmails.containsValue(email)) {
            log.warn("Пользователь пытается использовать уже занятый email");

            throw new AlreadyExistException(String.format("%s уже используется, выберите другой", email));
        }
    }

    private void getUserAndCheckDuplicateEmailWhenUpdateUser(User curUser, User user) {
        if (user.getEmail() == null) {
            return;
        }
        if (curUser.getEmail().equals(user.getEmail())) {
            return;
        }
        checkDuplicateEmailWhenCreateUser(user.getEmail());
    }

    @Override
    public void deleteUser(Integer userId) {
        users.remove(userId);
        usersEmails.remove(userId);
        log.info("User is deleted");
    }
}
