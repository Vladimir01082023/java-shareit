package ru.practicum.shareit.item;


import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentMapper;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemBookMapper {
    public static ItemBookDto toDtoWithBookings(Optional<Item> item, ItemBookDto bookDto, List<Comment> comments) {
        ItemBookDto itemDto = new ItemBookDto();
        itemDto.setId(item.get().getId());
        itemDto.setName(item.get().getName());
        itemDto.setDescription(item.get().getDescription());
        itemDto.setAvailable(item.get().getAvailable());
        itemDto.setOwnerId(item.get().getOwnerId());
        itemDto.setNextBooking(bookDto.getNextBooking());
        itemDto.setLastBooking(bookDto.getLastBooking());
        itemDto.setComments(comments.stream().map(CommentMapper::toDTO).collect(Collectors.toList()));
//        itemDto.setRequestId(item.getRequestId());
        return itemDto;
    }

    public static ItemBookDto toDtoWithoutBookings(Optional<Item> item, List<Comment> comments) {
        ItemBookDto itemDto = new ItemBookDto();
        itemDto.setId(item.get().getId());
        itemDto.setName(item.get().getName());
        itemDto.setDescription(item.get().getDescription());
        itemDto.setAvailable(item.get().getAvailable());
        itemDto.setOwnerId(item.get().getOwnerId());
        itemDto.setComments(comments.stream().map(CommentMapper::toDTO).collect(Collectors.toList()));
//        itemDto.setRequestId(item.getRequestId());
        return itemDto;
    }

    public static Item fromDto(ItemBookDto dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwnerId(dto.getOwnerId());
        return item;
    }
}
