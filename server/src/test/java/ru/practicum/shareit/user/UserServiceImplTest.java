package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ItemAvailableException;
import ru.practicum.shareit.exceptions.NotFoundUserItemExceptions;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private User user1;

    @BeforeEach
    void beforeEach() {
        user1 = new User(1L, "user1 name", "user1@testmail.com");
    }

    @Test
    public void getUser_shouldReturnUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        when(userRepository.existsById(1L)).thenReturn(true);

        UserDto result = userService.getUserById(1L);

        assertThat(user1.getName(), equalTo(result.getName()));
    }

    @Test
    public void findUserById_whenInvokedWitchUserIdNotFound_shouldShouldThrowException() {
        NotFoundUserItemExceptions exception = assertThrows(NotFoundUserItemExceptions.class, () ->
                userService.getUserById(99L));

        assertThat(exception.getMessage(), is("User with id 99 not found"));
    }

    @Test
    public void shouldGetAllUsers_shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user1));
        List<UserDto> result = userService.getAllUsers();
        assertThat(result.size(), equalTo(1));
    }

    @Test
    public void updateUser_shouldUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.save(any())).thenReturn(user1);
        UserDto updatedUser = userService.updateUser(new UserDto(1L, "user1 name",
                "user1@testmail.com"), 1L);
        assertThat(user1.getName(), equalTo(updatedUser.getName()));

    }

    @Test
    public void createUser_whenCreated_shouldReturnUserDto() {
        when(userRepository.save(any()))
                .thenReturn(user1);

        UserDto userDto = userService.saveUser(new UserDto(1L, "userDto", "userDto@mail.ry"));

        assertThat(user1.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    public void createUser_whenCreateWithIncorrectData_shouldShouldThrowException() {
        when(userRepository.getUserById(1L))
                .thenReturn(user1);

        ItemAvailableException exception = assertThrows(ItemAvailableException.class, () ->
                userService.saveUser(UserMapper.toUserDto(user1)));

        assertThat(exception.getMessage(), is("User with id 1 already exists"));
    }

    @Test
    public void updateUser_whenUpdatedWitchIncorrectData_shouldShouldThrowException() {
        Mockito.lenient().when(userRepository.findById(1L))
                .thenReturn(Optional.of(user1));
        UserDto updatedUserDto = new UserDto(1L, "noValid", null);

        NotFoundUserItemExceptions exception = assertThrows(NotFoundUserItemExceptions.class, () ->
                userService.updateUser(updatedUserDto, user1.getId()));

        assertThat(exception.getMessage(), is("User with id 1 not found"));
    }

    @Test
    public void updatedUser_whenUpdatedUserIdNotFound_shouldShouldThrowException() {
        UserDto userDto = new UserDto(99L, "test@test.com", "name");
        NotFoundUserItemExceptions exception = assertThrows(NotFoundUserItemExceptions.class, () ->
                userService.updateUser(userDto, userDto.getId()));

        assertThat(exception.getMessage(), is("User with id 99 not found"));
    }

    @Test
    public void updatedUser_whenEmailIsNull_shouldShouldThrowException() {
        UserDto userDto = new UserDto(1L, null, null);
        ItemAvailableException exception = assertThrows(ItemAvailableException.class, () ->
                userService.updateUser(userDto, 1L));

        assertThat(exception.getMessage(), is("Name and email can not be null"));
    }

    @Test
    public void deleteUser_whenDeleted_shouldReturnDeletedUserDto() {
        UserDto userDto = Mockito.mock(UserDto.class);
        userService.deleteUser(userDto.getId());
        Mockito.verify(userDto, Mockito.times(1))
                .getId();

    }
}
