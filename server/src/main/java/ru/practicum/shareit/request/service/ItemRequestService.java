package ru.practicum.shareit.request.service;


import ru.practicum.shareit.request.dto.ItemRequestReceiveDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    List<ItemRequest> getItemRequest(Long userId);

    ItemRequest createItemRequest(ItemRequestReceiveDto itemRequestReceiveDto, Long userId);

    List<ItemRequest> getAllRequests(Long userId, Integer from, Integer size);

    ItemRequest getItemRequestById(Long requestId,  Long userId);

}
