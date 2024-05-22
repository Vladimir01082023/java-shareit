package ru.practicum.shareit.user;

import java.util.List;

interface UserRepository {
    List<User> findAll();

    User save(User user);

    User update(User user, Integer userID);

    User getUserById(Integer id);

    void deleteUser(Integer id);
}
