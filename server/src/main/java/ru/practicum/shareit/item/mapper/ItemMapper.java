package ru.practicum.shareit.item.mapper;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

public class ItemMapper {

    public static ItemDto toDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwnerId(item.getOwnerId());
        itemDto.setRequestId(item.getRequestId());
        return itemDto;
    }

    public static Item fromDto(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setRequestId(itemDto.getRequestId());
        item.setOwnerId(itemDto.getOwnerId());
        return item;
    }

    public static ItemDto toDto(Optional<Item> item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.get().getId());
        itemDto.setName(item.get().getName());
        itemDto.setDescription(item.get().getDescription());
        itemDto.setAvailable(item.get().getAvailable());
        itemDto.setOwnerId(item.get().getOwnerId());
        itemDto.setRequestId(item.get().getRequestId());
        return itemDto;
    }

    public static ItemResponseDto toResponseDto(Item item) {
        ItemResponseDto itemResponseDto = new ItemResponseDto();
        itemResponseDto.setId(item.getId());
        itemResponseDto.setName(item.getName());
        itemResponseDto.setDescription(item.getDescription());
        itemResponseDto.setRequestId(item.getRequestId());
        itemResponseDto.setAvailable(item.getAvailable());
        return itemResponseDto;
    }
}


