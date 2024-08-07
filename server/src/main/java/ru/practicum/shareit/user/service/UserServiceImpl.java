package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ItemAvailableException;
import ru.practicum.shareit.exceptions.NotFoundUserItemExceptions;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        log.info("All users are gotten");
        return repository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundUserItemExceptions("User with id " + id + " not found");
        }
        return UserMapper.toUserDto(repository.findById(id));
    }

    @Override
    public UserDto saveUser(UserDto user) {
        if (repository.getUserById(user.getId()) != null) {
            throw new ItemAvailableException("User with id " + user.getId() + " already exists");
        }
        log.info("User {} is saved: ", user);
        return UserMapper.toUserDto(repository.save(UserMapper.fromUserDto(user)));
    }

    @Override
    public UserDto updateUser(UserDto updatedUser, Long userId) {
        log.info(String.format("Получен запрос на обновление пользовтаеля с id = {}", userId));

        if (updatedUser.getName() == null && updatedUser.getEmail() == null) {
            throw new ItemAvailableException("Name and email can not be null");
        }
        User curUser = UserMapper.fromUserDto(getUserById(userId));

        if (updatedUser.getName() != null && !curUser.getName().equals(updatedUser.getName())) {
            curUser.setName(updatedUser.getName());
        }

        if (updatedUser.getEmail() != null && !curUser.getEmail().equals(updatedUser.getEmail())) {
            curUser.setEmail(updatedUser.getEmail());
        }

        repository.save(curUser);

        log.info("Пользователь с id = {} успешно обновлен", userId);

        return UserMapper.toUserDto(curUser);
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
