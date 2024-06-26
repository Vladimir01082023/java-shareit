package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Mapper.BookDtoMapper;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.ItemAvailableException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotFoundUserItemExceptions;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.CommentMapper;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.user.UserRepository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final Logger log = Logger.getLogger(ItemServiceImpl.class.getName());
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;


    @Override
    public ItemDto addNewItem(Long userId, ItemDto item) {
        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            throw new NotFoundException("Description of item  can not null");
        }
        if (item.getAvailable() == null) {
            throw new NotFoundException("Item not available");
        }
        if (item.getName() == null || item.getName().isEmpty()) {
            throw new NotFoundException("Name of item can not be empty or null");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundUserItemExceptions("User not found");
        }
        item.setOwnerId(userId);
        return ItemMapper.toDto(itemRepository.save(ItemMapper.fromDto(item)));
    }

    @Override
    public List<ItemBookDto> getItems(Long userId) {
        List<Item> listItem = itemRepository.findItemByOwnerId(userId);
        List<ItemBookDto> listItemBookDTO = new ArrayList<>();
        for (Item item : listItem) {
            ItemBookDto b;
            if (checkItemOnBooking(item.getId())) {
                b = getItemWithBooking(item.getId(), userId);
                listItemBookDTO.add(b);
            } else {
                b = getItemByIdWithBook(item.getId());
                listItemBookDTO.add(b);
            }
        }
        return listItemBookDTO;
    }

    @Override
    public ItemDto getItemById(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new NotFoundUserItemExceptions("User not found");
        }
        return ItemMapper.toDto(itemRepository.findById(id));
    }

    @Override
    public List<ItemDto> getItemByText(String itemText) {

        if (itemRepository.getItemByText(itemText) != null) {
            return itemRepository.getItemByText(itemText).stream()
                    .map(ItemMapper::toDto)
                    .collect(Collectors.toList());
        } else
            throw new NotFoundException("Item not found");
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto item, Long itemId) {
        log.info(String.format("Получен запрос на обновление вещи с id = {}", itemId));

        Item currentItem = ItemMapper.fromDto(getItemById(itemId));

        if (!(currentItem.getOwnerId().equals(userId))) {
            throw new NotFoundUserItemExceptions("Id пользователя отличается от id владельца item");
        }

        if (item.getAvailable() != null) {
            currentItem.setAvailable(item.getAvailable());
        }
        if (item.getName() != null) {
            currentItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            currentItem.setDescription(item.getDescription());
        }
        itemRepository.save(currentItem);
        log.info("Item with id " + itemId + " has been updated");

        return ItemMapper.toDto(currentItem);
    }

    @Override
    public ItemBookDto getItemWithBooking(Long itemId, Long userId) {
        ItemBookDto itemBookDto = new ItemBookDto();
        List<Booking> listBookingBeforeCurrentTime = bookingRepository.getLastBookings(itemId, "APPROVED").stream()
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        List<Booking> listBookingAfterCurrentTime = bookingRepository.getNextBookings(itemId, "APPROVED").stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        if (listBookingBeforeCurrentTime.size() != 0) {
            itemBookDto.setLastBooking(BookDtoMapper.map(listBookingBeforeCurrentTime.get(0)));
        }
        if (listBookingAfterCurrentTime.size() != 0) {
            itemBookDto.setNextBooking(BookDtoMapper.map(listBookingAfterCurrentTime.get(listBookingAfterCurrentTime.size() - 1)));
        }
        return ItemBookMapper.toDtoWithBookings(itemRepository.findById(itemId), itemBookDto, commentRepository.findByItemId(itemId));
    }

    @Override
    public boolean checkItemOnBooking(Long itemID) {
        List<Booking> listOfBookings = bookingRepository.findBookingsByOwnerIdAndStatus(itemID);

        if (listOfBookings == null || listOfBookings.isEmpty() || listOfBookings.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public ItemBookDto getItemByIdWithBook(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new NotFoundUserItemExceptions("User not found");
        }
        List<Comment> s = commentRepository.findByItemId(id);
        return ItemBookMapper.toDtoWithoutBookings(itemRepository.findById(id), commentRepository.findByItemId(id));
    }

    @Override
    public CommentDto addComment(Long itemId, CommentDto commentDto, Long userId) {
        if (!checkUserOnCommentForItem(userId, itemId)) {
            throw new ItemAvailableException("User is never booked this item!");
        }
        if (commentDto.getText().isEmpty() || commentDto.getText() == null) {
            throw new ItemAvailableException("Text of comment is empty!");
        }
        Comment comment = CommentMapper.fromDTO(commentDto, itemRepository.findById(itemId).get(),
                userRepository.findById(userId).get());
        Comment saveComment = commentRepository.save(comment);
        return CommentMapper.toDTO(saveComment);
    }

    public boolean checkUserOnCommentForItem(Long userId, Long itemId) {
        List<Booking> listBookingsForUser = bookingRepository.getAllBookingsByBookerId(userId);
        Booking userBookingItem = listBookingsForUser.stream()
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()) && booking.getItem().getId().equals(itemId))
                .findFirst().orElse(null);
        return userBookingItem != null;
    }
}