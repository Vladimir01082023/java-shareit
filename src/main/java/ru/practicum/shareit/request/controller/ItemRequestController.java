package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ItemAvailableException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequest post(@RequestBody ItemRequestDto requestDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        if (requestDto.getDescription() == null) {
            throw new ItemAvailableException("Description cannot be empty");
        }
        return itemRequestService.createItemRequest(requestDto, userId);
    }

    @GetMapping
    public List<ItemRequest> get(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getItemRequest(userId);
    }

    @GetMapping("/all")
    public List<ItemRequest> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestParam(value = "from", required = false, defaultValue = "0")@PositiveOrZero Integer from,
                             @RequestParam(value = "size", required = false, defaultValue = "10")@Positive Integer size) {
            return itemRequestService.getAllRequests(userId, from, size);

    }

    @GetMapping("/{requestId}")
    public ItemRequest getRequestById(@PathVariable Long requestId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getItemRequestById(requestId, userId);
    }
}
