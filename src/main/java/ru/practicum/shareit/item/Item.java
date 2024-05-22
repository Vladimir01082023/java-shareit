package ru.practicum.shareit.item;


import lombok.AllArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import lombok.Data;


@Data
@AllArgsConstructor
public class Item {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
    private User owner;
}
