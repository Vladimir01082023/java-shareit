package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.DTO.BookingDTO;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;


public interface BookingService {
    Booking createBooking(Long itemID, BookingDTO bookingDTO);

    Booking getBooking(Long bookingId);

    Booking approveBooking(boolean approval, Long bookingID);

    List<Booking> getAllBookingsForUser(String state, Long userID, Integer from, Integer size);

    List<Booking> getAllBookingsForOwner(String state, Long userID, Integer from, Integer size);

}
