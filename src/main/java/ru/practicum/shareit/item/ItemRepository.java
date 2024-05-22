package ru.practicum.shareit.item;

import java.util.List;

interface ItemRepository {

    List<Item> findByUserId(Integer userId);

    Item save(Integer userID, Item item);

    void deleteByUserIdAndItemId(Integer userId, Integer itemId);

    Item getItemById(Integer id);

    Item updateItem(Integer id, Item item, Integer itemId);

    List<Item> getItemByText(String text, Integer userID);
}