package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotFoundUserItemExceptions;
import ru.practicum.shareit.user.UserRepository;


import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final Logger log = Logger.getLogger(ItemServiceImpl.class.getName());
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addNewItem(Long userId, ItemDto item) {
        if(item.getDescription() == null || item.getDescription().isEmpty()){
            throw new NotFoundException("Description of item  can not null");
        }
        if(item.getAvailable() == null){
            throw new NotFoundException("Item not available");
        }
        if(item.getName() == null || item.getName().isEmpty()){
            throw new NotFoundException("Name of item can not be empty or null");
        }
        if(!userRepository.existsById(userId)) {
            throw new NotFoundUserItemExceptions("User not found");
        }
        item.setOwnerId(userId);
        return ItemMapper.toDto(itemRepository.save(ItemMapper.fromDto(item)));
    }

    @Override
    public List<ItemDto> getItems(Long userId) {
        return itemRepository.findItemByOwnerId(userId).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long id) {
        if(!itemRepository.existsById(id)) {
            throw new NotFoundUserItemExceptions("User not found");
        }
        return ItemMapper.toDto(itemRepository.findById(id));
    }

    @Override
    public List<ItemDto> getItemByText(String itemText) {

        if (itemRepository.getItemByText(itemText) != null) {
            return itemRepository.getItemByText(itemText).stream()
                    .map(ItemMapper::toDto)
                    .collect(Collectors.toList());
        } else
            throw new NotFoundException("Item not found");
    }

//    @Override
//    public ItemDto getItemWithBooking(Long itemId, Long userId) {
//
//    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto item, Long itemId) {
        log.info(String.format("Получен запрос на обновление вещи с id = {}", itemId));

        Item currentItem = ItemMapper.fromDto(getItemById(itemId));

        if(currentItem.getOwnerId() != userId){
            throw new NotFoundUserItemExceptions("Id пользователя отличается от id владельца item");
        }

        if (item.getAvailable() != null) {
            currentItem.setAvailable(item.getAvailable());
        }
        if (item.getName() != null) {
            currentItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            currentItem.setDescription(item.getDescription());
        }
        itemRepository.save(currentItem);
        log.info("Item with id " + itemId + " has been updated");

        return ItemMapper.toDto(currentItem);
    }
}