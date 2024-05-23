package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/{itemID}")
    public ItemDto getItemById(@PathVariable("itemID") Integer itemID) {
        return itemService.getItemById(itemID);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByText(@RequestParam(value = "text", required = false) String text,
                                       @RequestHeader("X-Sharer-User-Id") Integer userId) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemService.getItemByText(text, userId);
    }

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Integer userId,
                       @RequestBody ItemDto item) {
        return itemService.addNewItem(userId, item);
    }

    @PatchMapping("/{itemID}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                              @RequestBody ItemDto item, @PathVariable Integer itemID) {
        return itemService.updateItem(userId, item, itemID);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @PathVariable Integer itemId) {
        itemService.deleteItem(userId, itemId);
    }
}
