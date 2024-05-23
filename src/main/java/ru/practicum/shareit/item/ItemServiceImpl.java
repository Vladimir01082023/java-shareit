package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public List<ItemDto> getItems(Integer userId) {
        return itemRepository.findByUserId(userId).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Integer id) {
        return ItemMapper.toDto(itemRepository.getItemById(id));
    }

    @Override
    public List<ItemDto> getItemByText(String itemText, Integer userId) {

        if (itemRepository.getItemByText(itemText, userId) != null) {
            return itemRepository.getItemByText(itemText, userId).stream()
                    .map(ItemMapper::toDto)
                    .collect(Collectors.toList());
        } else
            throw new NotFoundException("Item not found");
    }

    @Override
    public ItemDto addNewItem(Integer userId, ItemDto item) {
        return ItemMapper.toDto(itemRepository.save(userId, ItemMapper.fromDto(item)));

    }

    @Override
    public void deleteItem(Integer userId, Integer itemId) {
    }

    @Override
    public ItemDto updateItem(Integer userId, ItemDto item, Integer itemId) {
        return ItemMapper.toDto(itemRepository.updateItem(userId, ItemMapper.fromDto(item), itemId));
    }

}