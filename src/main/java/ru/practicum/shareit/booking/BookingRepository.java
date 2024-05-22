package ru.practicum.shareit.booking;

import java.time.LocalDateTime;
import java.util.List;

interface BookingRepository {
    Booking getBooking(Integer id);
    void saveBooking(Integer itemID, Integer userId);
    void deleteBooking(Booking booking);
    List<Booking> getBookings();
}
