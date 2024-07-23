package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Getting all items from user {}", userId);
        return itemClient.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId, @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Getting item {} from user {}", itemId, userId);
        return itemClient.getById(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItems(@RequestParam String text, @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Getting items with text {}", text);
        return itemClient.searchItem(text, userId);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestBody ItemDto item, @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Adding item {}", item);
        return itemClient.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long itemId, @RequestBody ItemDto item,
                                             @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Updating item {}", item);
        return itemClient.updateItem(userId, item, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Long itemId, @RequestBody CommentDto comment,
                                             @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Adding comment {}", comment);
        return itemClient.addComment(userId, comment, itemId);
    }
}
