package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotFoundUserItemExceptions;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public List<Item> getItems(Integer userId) {
        return itemRepository.findByUserId(userId);
    }

    @Override
    public Item getItemById(Integer itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public List<Item> getItemByText(String itemText, Integer userId) {

        if(itemRepository.getItemByText(itemText, userId) != null){
            return itemRepository.getItemByText(itemText, userId);
        }
        else
            throw new NotFoundException("Item not found");
    }

    @Override
    public Item addNewItem(Integer userId, Item item) {
        itemRepository.save(userId, item);
        return item;
    }

    @Override
    public void deleteItem(Integer userId, Integer itemId) {
    }

    @Override
    public Item updateItem(Integer userId, Item item, Integer itemId) {
        return itemRepository.updateItem(userId, item, itemId);
    }

}