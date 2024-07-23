package ru.practicum.shareit.item.service;



import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(Long userId, ItemDto item);

    List<ItemBookDto> getItems(Long userId);

    ItemDto updateItem(Long userId, ItemDto item, Long itemId);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getItemByText(String itemText);

    ItemBookDto getItemWithBooking(Long itemId, Long userId);

    boolean checkItemOnBooking(Long itemID);

    ItemBookDto getItemByIdWithBook(Long id);

    CommentDto addComment(Long itemId, CommentDto commentDto, Long userId);
}
