package ru.practicum.shareit.item;


import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.Valid;

@Data
public class ItemDto {
    @Valid
    private Integer id;
    @Valid
    private String name;
    @Valid
    private String description;
    @Valid
    private Boolean available;
    @Valid
    private ItemRequest request;
    @Valid
    private User owner;
}
