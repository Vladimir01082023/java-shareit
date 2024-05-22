package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<Item> get(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/{itemID}")
    public Item getItemById(@PathVariable("itemID") Integer itemID) {
        return itemService.getItemById(itemID);
    }

    @GetMapping("/search")
    public List<Item> getItemByText(@RequestParam("text") String text, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemService.getItemByText(text, userId);
    }

    @PostMapping
    public Item add(@RequestHeader("X-Sharer-User-Id") Integer userId,
                    @RequestBody Item item) {
        return itemService.addNewItem(userId, item);
    }

    @PatchMapping("/{itemID}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @RequestBody Item item, @PathVariable Integer itemID) {
        return itemService.updateItem(userId, item, itemID);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @PathVariable Integer itemId) {
        itemService.deleteItem(userId, itemId);
    }
}
