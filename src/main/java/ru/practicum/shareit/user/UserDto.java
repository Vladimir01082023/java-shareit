package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;

@Data
@AllArgsConstructor
public class UserDto {
    @Valid
    private  Integer id;
    @Valid
    private  String name;
    @Valid
    private  String email;
}
