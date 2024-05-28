package ru.practicum.shareit.user;

import java.util.List;

interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Integer id);

    UserDto saveUser(UserDto user);

    UserDto updateUser(UserDto user, Integer userID);

    void deleteUser(Integer id);
}
