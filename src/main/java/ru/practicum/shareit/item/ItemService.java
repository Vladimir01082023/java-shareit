package ru.practicum.shareit.item;

import java.util.List;

interface ItemService {
    List<ItemDto> getItems(Integer userId);

    ItemDto addNewItem(Integer userId, ItemDto item);

    void deleteItem(Integer userId, Integer itemId);

    ItemDto updateItem(Integer userId, ItemDto item, Integer itemId);

    ItemDto getItemById(Integer itemId);

    List<ItemDto> getItemByText(String itemText, Integer userId);

}
