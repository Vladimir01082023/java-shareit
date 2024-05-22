package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public void createBooking(Integer itemID, Integer userId) {
        bookingRepository.saveBooking(itemID, userId);
    }

    @Override
    public Booking getBooking(Integer bookingId) {
        return null;
    }

    @Override
    public List<Booking> getBookings() {
        return List.of();
    }

    @Override
    public void deleteBooking(Integer bookingId) {

    }
}
