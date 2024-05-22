package ru.practicum.shareit.item;

import java.util.List;

interface ItemService {
    List<Item> getItems(Integer userId);

    Item addNewItem(Integer userId, Item item);

    void deleteItem(Integer userId, Integer itemId);

    Item updateItem(Integer userId, Item item, Integer itemId);

    Item getItemById(Integer itemId);

    List<Item> getItemByText(String itemText, Integer userId);

}
