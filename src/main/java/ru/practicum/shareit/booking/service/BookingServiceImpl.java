package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.DTO.BookingDTO;
import ru.practicum.shareit.booking.Mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.ItemAvailableException;
import ru.practicum.shareit.exceptions.NotFoundUserItemExceptions;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final LocalDateTime curTime = LocalDateTime.now();
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Override
    public Booking createBooking(Long userID, BookingDTO bookingDTO) {
        ItemDto curItem = itemService.getItemById(bookingDTO.getItemId());
        if (userService.getUserById(userID) == null) {
            throw new NotFoundUserItemExceptions("User not found");
        }
        if (userID.equals(curItem.getOwnerId())) {
            throw new NotFoundUserItemExceptions("Owner can not book his own item");
        }
        if (curItem == null) {
            throw new NotFoundUserItemExceptions("Item is not found");
        }
        if (curItem.getAvailable().equals(false)) {
            throw new ItemAvailableException("Item is not available");
        }
        if (bookingDTO.getEnd() == null || bookingDTO.getStart() == null) {
            throw new ItemAvailableException("End/start time is null");
        }
        if (bookingDTO.getEnd().isBefore(bookingDTO.getStart()) || bookingDTO.getEnd().isEqual(bookingDTO.getStart())) {
            throw new ItemAvailableException("End time is before start time or they are equal");
        }
        if (bookingDTO.getStart().isBefore(curTime)) {
            throw new ItemAvailableException("Start time is before current time");
        }

        log.info("Booking of item {} from user {} is saved: ", bookingDTO.getItemId(), userID);
        bookingDTO.setBookerId(userID);
        Booking booking = BookingMapper.fromBookerDto(bookingDTO, ItemMapper.fromDto(itemService.getItemById(bookingDTO.getItemId())),
                UserMapper.fromUserDto(userService.getUserById(userID)));
        booking.setStatus(Status.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBooking(Long bookingId) {
        return bookingRepository.getBookingById(bookingId);
    }

    @Override
    public Booking approveBooking(boolean approval, Long bookingId) {
        if (bookingRepository.getBookingById(bookingId).getStatus().equals(Status.APPROVED)) {
            throw new ItemAvailableException("Booking is already approved");
        }
        Status status;
        if (approval) {
            status = Status.APPROVED;
        } else {
            status = Status.REJECTED;
        }
        Booking booking = bookingRepository.getBookingById(bookingId);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAllBookingsForOwner(String state, Long userID, Integer from, Integer size) {
        if (userService.getUserById(userID) == null) {
            throw new NotFoundUserItemExceptions("User not found");
        }
        log.info("Получен запрос на получение всех бронирований владельца {}", userID);
        if (state == null || state.equals("ALL")) {
            return bookingRepository.findBookingsByOwnerId(userID, PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                    .sorted(Comparator.comparing(Booking::getStart, Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
        } else if (state.equals("FUTURE")) {
            return bookingRepository.findBookingsByOwnerId(userID, PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                    .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Booking::getStart, Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
        } else if (state.equals("PAST")) {
            return bookingRepository.findBookingsByOwnerId(userID, PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                    .sorted(Comparator.comparing(Booking::getStart, Comparator.nullsLast(Comparator.reverseOrder())))
                    .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                    .collect(Collectors.toList());
        } else if (state.equals("CURRENT")) {
            return bookingRepository.findBookingsByOwnerId(userID, PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                    .sorted(Comparator.comparing(Booking::getStart, Comparator.nullsLast(Comparator.reverseOrder())))
                    .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
        } else if (state.equals("WAITING") || state.equals("CURRENT") || state.equals("REJECTED") || state.equals("PAST")) {
            return bookingRepository.findBookingsByOwnerIdAndState(userID, state, PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                    .sorted(Comparator.comparing(Booking::getStart, Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
        } else {
            throw new ItemAvailableException("Unknown state: " + state);
        }
    }

    @Override
    public List<Booking> getAllBookingsForUser(String state, Long userID, Integer from, Integer size) {
        if (userService.getUserById(userID) == null) {
            throw new NotFoundUserItemExceptions("User not found");
        }
        log.info("Получен запрос на получение всех бронирований пользователя {}", userID);
        if (state == null || state.equals("ALL")) {
            return bookingRepository.getAllBookingsByBookerIdOrderByStartDesc(userID, PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                    .sorted(Comparator.comparing(Booking::getStart, Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
        } else if (state.equals("FUTURE")) {
            return bookingRepository.getAllBookingsByBookerIdOrderByStartDesc(userID, PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                    .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Booking::getStart, Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
        } else if (state.equals("PAST")) {
            return bookingRepository.getAllBookingsByBookerIdOrderByStartDesc(userID, PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                    .sorted(Comparator.comparing(Booking::getStart, Comparator.nullsLast(Comparator.reverseOrder())))
                    .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                    .collect(Collectors.toList());
        } else if (state.equals("CURRENT")) {
            return bookingRepository.getAllBookingsByBookerIdOrderByStartDesc(userID, PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                    .sorted(Comparator.comparing(Booking::getStart, Comparator.nullsLast(Comparator.reverseOrder())))
                    .filter(booking -> (booking.getStart().isBefore(LocalDateTime.now()) ||
                            booking.getStart().isEqual(LocalDateTime.now())) &&
                            booking.getEnd().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
        } else if (state.equals("WAITING") || state.equals("REJECTED")) {
            return bookingRepository.findByBookerIdAndStatusEquals(userID, state, PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                    .sorted(Comparator.comparing(Booking::getStart, Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
        } else {
            throw new ItemAvailableException("Unknown state: " + state);
        }
    }
}
