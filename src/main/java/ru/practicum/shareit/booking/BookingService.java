package ru.practicum.shareit.booking;


import ru.practicum.shareit.booking.DTO.BookingDTO;

import java.util.List;


public interface BookingService {
    Booking createBooking(Long itemID, BookingDTO bookingDTO);

    Booking getBooking(Long bookingId);

    Booking approveBooking(boolean approval, Long bookingID);

    List<Booking> getAllBookingsForUser(String state, Long userID);

    List<Booking> getAllBookingsForOwner(String state, Long userID);

}
