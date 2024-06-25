package ru.practicum.shareit.item;


import lombok.Data;
import ru.practicum.shareit.booking.DTO.BookDto;
import ru.practicum.shareit.item.comments.CommentDto;

import java.util.ArrayList;
import java.util.List;


@Data
public class ItemBookDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    //    private Long requestId;
    private Long ownerId;
    private BookDto lastBooking;
    private BookDto nextBooking;
    private List<CommentDto> comments = new ArrayList<>();
}