package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDto saveNewUser(@RequestBody @Valid UserDto user) {
        return userService.saveUser(user);
    }

    @PatchMapping("/{userID}")
    public UserDto updateUser(@RequestBody UserDto user, @PathVariable Long userID) {
        return userService.updateUser(user, userID);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
