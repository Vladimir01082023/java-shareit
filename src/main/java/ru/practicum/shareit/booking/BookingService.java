package ru.practicum.shareit.booking;

import java.time.LocalDateTime;
import java.util.List;

interface BookingService {
    void createBooking(Integer itemID, Integer userId);
    Booking getBooking(Integer bookingId);
    List<Booking> getBookings();
    void deleteBooking(Integer bookingId);
}
