package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotFoundUserItemExceptions;
import ru.practicum.shareit.user.UserRepositoryImpl;


import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Integer, List<Item>> items = new HashMap<>();
    private Integer id = 1;
    private final UserRepositoryImpl userRepository;
    private static final Logger log = LoggerFactory.getLogger(ItemRepositoryImpl.class);

    private Integer generateId() {
        return this.id++;
    }

    @Override
    public List<Item> findByUserId(Integer userId) {
        return items.get(userId);
    }

    @Override
    public Item save(Integer userID, Item item) {

        checkUserPresence(userID);
        checkItemName(item.getName());
        checkItemAvailability(item);
        checkItemDescription(item);

        item.setId(generateId());
        item.setOwner(userRepository.getUserById(userID));

        items.compute(userID, (userId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });

        return item;
    }

    @Override
    public void deleteByUserIdAndItemId(Integer userId, Integer itemId) {
        if (items.containsKey(userId)) {
            List<Item> userItems = items.get(userId);
            userItems.removeIf(item -> item.getId().equals(itemId));
        }
    }

    @Override
    public Item getItemById(Integer id) {
        List<Item> listItems = new ArrayList<>();
        for (List<Item> value : items.values()) {
            listItems.addAll(value);
        }
        Item item = listItems.stream()
                .filter(item1 -> item1.getId().intValue() == id.intValue())
                .findFirst().orElse(null);
        return item;
    }

    @Override
    public List<Item> getItemByText(String text, Integer userID) {
        List<Item> userItems = new ArrayList<>();
        for (List<Item> value : items.values()) {
            for (Item item : value) {
                userItems.add(item);
            }
        }

        List<Item> item = userItems.stream()
                .filter(item1 -> item1.getDescription().toLowerCase().contains(text.toLowerCase()) &&
                        item1.getAvailable().equals(true) ||
                        item1.getName().toLowerCase().contains(text.toLowerCase()) && item1.getAvailable().equals(true))
                .collect(Collectors.toList());
        return item;
    }

    @Override
    public Item updateItem(Integer userId, Item item, Integer itemId) {
        log.info("Updating item with id " + itemId);

        checkUserPresence(userId);
        checkItemOwner(userId, itemId);

        Item curItem = getItemById(itemId);

        if (item.getAvailable() != null) {
            curItem.setAvailable(item.getAvailable());
        }
        if (item.getName() != null) {
            curItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            curItem.setDescription(item.getDescription());
        }

        log.info("Item with id " + itemId + " has been updated");

        return curItem;
    }

    public void checkItemName(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            log.warn("Item's name cannot be null or empty");
            throw new NotFoundException("Item's name cannot be null or empty");
        }
    }

    public void checkUserPresence(Integer userId) {
        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundUserItemExceptions("User not found");
        }
    }

    public void checkItemAvailability(Item item) {
        if (item.getAvailable() == null) {
            throw new NotFoundException("Field available is null");
        }
    }

    public void checkItemDescription(Item item) {
        if (item.getDescription() == null) {
            throw new NotFoundException("Field description is null");
        }
    }

    public void checkItemOwner(Integer ownerID, Integer itemID) {
        List<Item> userItems = items.get(ownerID);
        if (userItems == null) {
            throw new NotFoundUserItemExceptions("User not found");
        }
        if (!userItems.contains(getItemById(itemID))) {
            throw new NotFoundException(String.format("Item with id %s does not belong to user with id %s",
                    itemID, ownerID));
        }
    }
}
