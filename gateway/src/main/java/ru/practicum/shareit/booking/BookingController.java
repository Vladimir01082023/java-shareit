package ru.practicum.shareit.booking;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.model.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getAllBookingForUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking for user with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getAllBookingForUser(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestBody @Valid BookingDTO bookingDTO) {
        log.info("Creating booking {}, userId={}", bookingDTO, userId);
        return bookingClient.bookItem(userId, bookingDTO);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingForOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking for owner with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getAllBookingForOwner(userId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@PathVariable("bookingId") Long bookingId,
                                                 @RequestParam(value = "approved", required = true) boolean approved,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Approving booking {}, userId={}", bookingId, userId);
        return bookingClient.approveBooking(bookingId, approved, userId);
    }

}