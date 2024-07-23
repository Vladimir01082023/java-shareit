package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto saveUser(UserDto user);

    UserDto updateUser(UserDto user, Long userID);

    void deleteUser(Long id);
}
