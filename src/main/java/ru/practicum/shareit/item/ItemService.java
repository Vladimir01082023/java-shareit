package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItems(Long userId);

    ItemDto addNewItem(Long userId, ItemDto item);

    void deleteItem(Long userId, Long itemId);

    ItemDto updateItem(Long userId, ItemDto item, Long itemId);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getItemByText(String itemText);

}
