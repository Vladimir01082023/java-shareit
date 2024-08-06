package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.dto.BookingDtoFromClient;
import ru.practicum.shareit.exceptions.NotFoundUserItemExceptions;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto booking(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody BookingDtoFromClient bookingDTO) {
        return BookingMapper.toBookingDto(bookingService.createBooking(userId, bookingDTO));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable("bookingId") Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        if (bookingService.getBooking(bookingId) == null) {
            throw new NotFoundUserItemExceptions("Booking with id " + bookingId + " not found");
        }
        Booking booking = bookingService.getBooking(bookingId);
        Item curItem = booking.getItem();
        User booker = booking.getBooker();
        Long owner = curItem.getOwnerId();
        if (booker.getId().equals(userId) || owner.equals(userId)) {
            return BookingMapper.toBookingDto(bookingService.getBooking(bookingId));
        } else {
            throw new NotFoundUserItemExceptions("Only user or booker can view this booking");
        }

    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable("bookingId") Long bookingId,
                                     @RequestParam(value = "approved", required = true) boolean approved,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        Booking booking = bookingService.getBooking(bookingId);
        Item curItem = booking.getItem();
        if (!(curItem.getOwnerId().equals(userId))) {
            throw new NotFoundUserItemExceptions("Only owner of an item can approve this booking");
        }
        return BookingMapper.toBookingDto(bookingService.approveBooking(approved, bookingId));

    }

    @GetMapping
    public List<BookingDto> getAllBookingForUser(@RequestParam(value = "state", required = false) String state,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(value = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(value = "size", required = false, defaultValue = "10") @Positive Integer size) {
        return bookingService.getAllBookingsForUser(state, userId, from, size).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForOwner(@RequestParam(value = "state", required = false) String state,
                                                   @RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(value = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(value = "size", required = false, defaultValue = "10") @Positive Integer size) {

        return bookingService.getAllBookingsForOwner(state, userId, from, size).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}