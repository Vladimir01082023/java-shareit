package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;


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

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable Long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public Comment addComment(@PathVariable Long itemId, @RequestBody CommentDto comment,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.addComment(itemId, comment, userId);

    }

}
