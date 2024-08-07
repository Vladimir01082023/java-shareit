package ru.practicum.shareit.request.mapper;


import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestReceiveDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequestReceiveDto toDto(Item item) {
        ItemRequestReceiveDto dto = new ItemRequestReceiveDto();
        dto.setDescription(item.getDescription());
        return dto;
    }

    public static ItemRequest fromDto(ItemRequestReceiveDto dto, Long userId, LocalDateTime localDateTime) {
        ItemRequest item = new ItemRequest();
        item.setId(dto.getId());
        item.setDescription(dto.getDescription());
        item.setRequestor(userId);
        item.setCreated(localDateTime);
        return item;
    }

    public static ItemRequestResponseDto toItemRequestResponseDto(ItemRequest item, List<ItemResponseDto> listResponces) {
        ItemRequestResponseDto dto = new ItemRequestResponseDto();
        dto.setStart(item.getCreated());
        dto.setDescription(item.getDescription());
        dto.setResponses(listResponces);
        return dto;
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setRequestor(itemRequest.getRequestor());
        dto.setCreated(itemRequest.getCreated());
        dto.setItems(itemRequest.getItems());
        return dto;
    }

}
