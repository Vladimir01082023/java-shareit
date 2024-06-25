package ru.practicum.shareit.user;


import java.util.Optional;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User fromUserDto(UserDto user) {
        return new User(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static UserDto toUserDto(Optional<User> user) {
        return new UserDto(
                user.get().getId(),
                user.get().getName(),
                user.get().getEmail());
    }
}
