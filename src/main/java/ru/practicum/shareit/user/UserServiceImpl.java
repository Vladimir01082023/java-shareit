package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UserEmailUniqueException;

import javax.validation.ValidationException;
import java.util.List;


@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository repository;

    @Override
    public List<User> getAllUsers() {
        log.info("All users are gotten");
        return repository.findAll();
    }

    @Override
    public User getUserById(Integer id) {
        return repository.getUserById(id);
    }

    @Override
    public User saveUser(User user) {
        if (user.getEmail() == null) {
            throw new NotFoundException("User must have an email address");
        }

        if (!checkEmail(user.getEmail())) {
            throw new ValidationException("User with email " + user.getEmail() + " not found");
        }

        if (!checkEmailPresence(user)) {
            log.info("User {} is saved: ", user);
            return repository.save(user);
        } else {
            throw new UserEmailUniqueException("User " + user.getName() + " is already saved");
        }
    }

    @Override
    public User updateUser(User user, Integer userID) {
        if (repository.getUserById(userID) != null) {
            log.info("User is updated");
            return repository.update(user, userID);
        } else {
            throw new NotFoundException(String.format("User with ID %d does not exist", user.getId()));
        }
    }

    public boolean checkEmailPresence(User user) {
        List<User> users = repository.findAll();
        User a = users.stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .findAny().orElse(null);
        if (a == null) {
            return false;
        } else
            return true;
    }

    public boolean checkEmail(String email) {
        if (email.contains("@")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteUser(Integer id) {
        repository.deleteUser(id);
    }
}
