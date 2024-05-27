package ru.practicum.shareit.item;


import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;


@Data
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
    private User owner;
}
