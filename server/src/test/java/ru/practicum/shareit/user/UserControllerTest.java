package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exceptions.NotFoundUserItemExceptions;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;
    @MockBean
    UserRepository userRepository;

    @Autowired
    private MockMvc mvc;

    private final UserDto userDto = new UserDto(1L, "testName", "testEmail@mail.ru");

    @Test
    public void saveNewUser_whenCreated_thenStatusOkAndMockDtoEqualsResponseDto() throws Exception {
        when(userService.saveUser(any()))
                .thenReturn(userDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    public void updateUser_whenUpdated_thenStatusOkAndMockDtoEqualsResponseDto() throws Exception {
        UserDto updateUserDto = new UserDto(1L, "newUser", "testEmail@mail.ru");

        when(userService.updateUser(updateUserDto, userDto.getId()))
                .thenReturn(new UserDto(1L, "newUser", "testEmail@mail.ru"));

        mvc.perform(patch("/users/{userId}", userDto.getId())
                        .content(mapper.writeValueAsString(updateUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updateUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updateUserDto.getName())))
                .andExpect(jsonPath("$.email", is(updateUserDto.getEmail())));
    }

    @Test
    public void findAllUsers_whenInvoked_thenStatusOkAndMockCollectionDtoEqualsResponseCollectionDto() throws Exception {
        List<UserDto> userList = List.of(userDto);

        when(userService.getAllUsers())
                .thenReturn(userList);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())));
    }

    @Test
    public void findUserById_whenUserFound_thenStatusOkAndMockDtoEqualsResponseDto() throws Exception {
        when(userService.getUserById(userDto.getId()))
                .thenReturn(userDto);

        mvc.perform(get("/users/{userId}", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    public void findUserById_whenUserNotFound_thenStatusNotFoundAndShouldThrowsUserNotFoundException() throws Exception {
        when(userService.getUserById(99L))
                .thenThrow(new NotFoundUserItemExceptions("Пользователь с id 99 не найден"));

        mvc.perform(get("/users/{userId}", 99))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass()
                        .equals(NotFoundUserItemExceptions.class));
    }

    @Test
    public void deleteUser_whenInvoked_thenStatusOkAndMockDtoEqualsResponseDtoAndResponseCollectionSizeEqualsZero()
            throws Exception {

        mvc.perform(delete("/users/{userId}", 1L))
                .andExpect(status().isOk());

        when(userService.getUserById(1L))
                .thenThrow(new NotFoundUserItemExceptions("Пользователь с id 1 не найден"));

        mvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass()
                        .equals(NotFoundUserItemExceptions.class));
    }
}
