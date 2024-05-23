package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UserEmailUniqueException;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
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
    public UserDto getUserById(Integer id) {
        return UserMapper.toUserDto(repository.getUserById(id));
    }

    @Override
    public UserDto saveUser(UserDto user) {
        if (user.getEmail() == null) {
            throw new NotFoundException("User must have an email address");
        }

        if (!checkEmail(user.getEmail())) {
            throw new ValidationException("User with email " + user.getEmail() + " not found");
        }

        if (!checkEmailPresence(UserMapper.fromUserDto(user))) {
            log.info("User {} is saved: ", user);
            return UserMapper.toUserDto(repository.save(UserMapper.fromUserDto(user)));
        } else {
            throw new UserEmailUniqueException("User " + user.getName() + " is already saved");
        }
    }

    @Override
    public UserDto updateUser(UserDto user, Integer userID) {
        if (repository.getUserById(userID) != null) {
            log.info("User is updated");
            return UserMapper.toUserDto(repository.update(UserMapper.fromUserDto(user), userID));
        } else {
            throw new NotFoundException(String.format("User with ID %d does not exist", user.getId()));
        }
    }

    public boolean checkEmailPresence(User user) {
        List<User> users = repository.findAll();
        User a = users.stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .findAny().orElse(null);
        return a != null;
    }

    public boolean checkEmail(String email) {
        return email.contains("@");
    }

    @Override
    public void deleteUser(Integer id) {
        repository.deleteUser(id);
    }
}
