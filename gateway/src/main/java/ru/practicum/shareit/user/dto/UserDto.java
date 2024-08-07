package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private Long userId;
    @NotNull(message = "Name can not be null")
    @NotBlank(message = "Name can not be blank")
    private String name;
    @Email
    @NotNull(message = "Email can not be null")
    @NotBlank(message = "Email can not be blank")
    private String email;
}