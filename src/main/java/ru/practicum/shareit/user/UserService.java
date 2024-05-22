package ru.practicum.shareit.user;

import java.util.List;

interface UserService {
    List<User> getAllUsers();

    User getUserById(Integer id);

    User saveUser(User user);

    User updateUser(User user, Integer userID);

    void deleteUser(Integer id);
}
