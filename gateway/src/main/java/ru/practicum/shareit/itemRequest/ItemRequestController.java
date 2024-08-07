package ru.practicum.shareit.itemRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.itemRequest.dto.ItemRequestReceiveDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> post(@RequestBody ItemRequestReceiveDto requestDto,
                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Creating new item request");
        return itemRequestClient.post(requestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Getting item request of user {}", userId);
        return itemRequestClient.get(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(value = "from", required = false, defaultValue = "0")
                                         @PositiveOrZero Integer from,
                                         @RequestParam(value = "size", required = false, defaultValue = "10")
                                         @Positive Integer size) {
        log.info("Getting all item requests");
        return itemRequestClient.getAll(userId, PageRequest.of(from, size));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable Long requestId,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Getting request id {}", requestId);
        return itemRequestClient.getRequestById(requestId, userId);
    }
}
