package ru.practicum.shareit.item.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookDto;
import ru.practicum.shareit.item.comments.CommentDto;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemBookDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private BookDto lastBooking;
    private BookDto nextBooking;
    private List<CommentDto> comments = new ArrayList<>();
}