package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoFromClient;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;


public interface BookingService {
    Booking createBooking(Long itemID, BookingDtoFromClient bookingDTO);

    Booking getBooking(Long bookingId);

    Booking approveBooking(boolean approval, Long bookingID);

    List<Booking> getAllBookingsForUser(String state, Long userID, Integer from, Integer size);

    List<Booking> getAllBookingsForOwner(String state, Long userID, Integer from, Integer size);

}