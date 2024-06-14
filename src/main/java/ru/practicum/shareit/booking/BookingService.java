package ru.practicum.shareit.booking;


import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking createBooking(Long itemID, BookingDTO bookingDTO);

    Booking getBooking(Long bookingId);

    List<Booking> getBookings();

    void deleteBooking(Integer bookingId);

    Booking approveBooking(boolean approval, Long bookingID);

    List<Booking> getAllBookingsForUser(String state, Long userID);

    List<Booking> getAllBookingsForOwner(String state, Long userID);

}
