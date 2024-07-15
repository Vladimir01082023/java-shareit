package ru.practicum.shareit.user.dto;

import lombok.*;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserDto {
    private Long id;
    @NotNull(message = "Name can not be null")
    @NotBlank(message = "Name can not be blank")
    private String name;
    @Email
    @NotNull(message = "Email can not be null")
    @NotBlank(message = "Email can not be blank")
    private String email;
}
