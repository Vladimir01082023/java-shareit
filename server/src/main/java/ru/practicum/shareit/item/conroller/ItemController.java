package ru.practicum.shareit.item.conroller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemBookDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;


import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemBookDto> get(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/{itemID}")
    public ItemBookDto getItemById(@PathVariable("itemID") Long itemID, @RequestHeader("X-Sharer-User-Id") Long userId) {
        if (userId.equals(itemService.getItemById(itemID).getOwnerId()) && itemService.checkItemOnBooking(itemID)) {
            return itemService.getItemWithBooking(itemID, userId);
        }
        return itemService.getItemByIdWithBook(itemID);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByText(@RequestParam(value = "text", required = false) String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemService.getItemByText(text);
    }

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @RequestBody ItemDto item) {
        return itemService.addNewItem(userId, item);
    }

    @PatchMapping("/{itemID}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody ItemDto item, @PathVariable Long itemID) {
        return itemService.updateItem(userId, item, itemID);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId, @RequestBody CommentDto comment,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.addComment(itemId, comment, userId);

    }

}
