package ru.practicum.shareit.item;


import ru.practicum.shareit.item.comments.CommentDto;

import java.util.List;

public interface ItemService {
    List<ItemBookDto> getItems(Long userId);

    ItemDto addNewItem(Long userId, ItemDto item);

    ItemDto updateItem(Long userId, ItemDto item, Long itemId);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getItemByText(String itemText);

    ItemBookDto getItemWithBooking(Long itemId, Long userId);

    boolean checkItemOnBooking(Long itemID);

    ItemBookDto getItemByIdWithBook(Long id);

    CommentDto addComment(Long itemId, CommentDto commentDto, Long userId);
}
