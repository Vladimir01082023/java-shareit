package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundUserItemExceptions;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final ItemService itemService;

    @PostMapping
    public Booking booking(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody BookingDTO bookingDTO) {
        return bookingService.createBooking(userId, bookingDTO);
    }

    @GetMapping("/{bookingId}")
    public Booking getBooking(@PathVariable("bookingId") Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        if (bookingService.getBooking(bookingId) == null) {
            throw new NotFoundUserItemExceptions("Booking with id " + bookingId + " not found");
        }
        Booking booking = bookingService.getBooking(bookingId);
        Item curItem = booking.getItem();
        User booker = booking.getBooker();
        Long owner = curItem.getOwnerId();
        if (booker.getId() == userId || owner == userId) {
            return bookingService.getBooking(bookingId);
        } else {
            throw new NotFoundUserItemExceptions("Only user or booker can view this booking");
        }

    }

    @PatchMapping("{bookingId}")
    public Booking approveBooking(@PathVariable("bookingId") Long bookingId,
                                  @RequestParam(value = "approved", required = true) boolean approved,
                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        Booking booking = bookingService.getBooking(bookingId);
        Item curItem = booking.getItem();
        if (curItem.getOwnerId() != userId) {
            throw new NotFoundUserItemExceptions("Only owner of an item can approve this booking");
        }
        return bookingService.approveBooking(approved, bookingId);

    }

    @GetMapping
    public List<Booking> getAllBookingForUser(@RequestParam(value = "state", required = false) String state,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getAllBookingsForUser(state, userId);
    }

    @GetMapping("/owner")
    public List<Booking> getAllBookingsForOwner(@RequestParam(value = "state", required = false) String state,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getAllBookingsForOwner(state, userId);
    }
}
