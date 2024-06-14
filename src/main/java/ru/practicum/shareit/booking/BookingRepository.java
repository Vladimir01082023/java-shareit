package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking getBookingById(Long bookingId);


    @Query(value = "select * from BOOKING WHERE BOOKER_ID  = ?1 and BOOK_STATUS = ?2",
            nativeQuery = true)
    List<Booking> getAllBookingsForUser(Long userId, String bookingStatus);

    List<Booking> getAllBookingsByBookerId(Long bookerId);

    @Query(value = "select * from BOOKING as book inner join items as it on book.BOOK_ITEM_ID = it.ITEM_ID " +
            "where it.ITEM_OWNER_ID = ?1", nativeQuery = true)
    List<Booking> findBookingsByOwnerId(Long ownerId);

    @Query(value = "select * from BOOKING as book inner join items as it on book.BOOK_ITEM_ID = it.ITEM_ID " +
            "where it.ITEM_OWNER_ID = ?1 and book.BOOK_STATUS = ?2", nativeQuery = true)
    List<Booking> findBookingsByOwnerIdAndState(Long itemId, String state);

    @Query(value = "select * from BOOKING as book inner join items as it on book.BOOK_ITEM_ID = it.ITEM_ID" +
            "where it.ITEM_ID = ?", nativeQuery = true)
    List<Booking> findBookingsForItem(Long itemId);
}
