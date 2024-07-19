package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking getBookingById(Long bookingId);


    @Query(value = "select * from BOOKING WHERE BOOKER_ID  = ?1 and BOOK_STATUS = ?2",
            nativeQuery = true)
    Page<Booking> findByBookerIdAndStatusEquals(Long userId, String bookingStatus, Pageable pageable);

    Page<Booking> getAllBookingsByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    List<Booking> getAllBookingsByBookerId(Long bookerId);

    Page<Booking> findByItemOwnerIdOrderByStartDesc(Long itemOwnerId, Pageable pageable);

    @Query(value = "select * from BOOKING as book inner join items as it on book.BOOK_ITEM_ID = it.ITEM_ID " +
            "where it.ITEM_OWNER_ID = ?1 and book.BOOK_STATUS = ?2 ORDER BY book.BOOK_START_DATE DESC", nativeQuery = true)
    Page<Booking> findBookingsByOwnerIdAndState(Long itemId, String state, Pageable pageable);

    @Query(value = "select * from BOOKING where book_item_id = ?1 and book_status = ?2 " +
            "ORDER BY book_start_date DESC", nativeQuery = true)
    List<Booking> getLastBookings(Long itemId, String status);

    @Query(value = "select * from BOOKING where book_item_id = ?1 and book_status = ?2 ORDER BY book_start_date DESC", nativeQuery = true)
    List<Booking> getNextBookings(Long itemId, String status);

    @Query(value = "select * from BOOKING where book_item_id = ?1 AND BOOK_STATUS = 'APPROVED'", nativeQuery = true)
    List<Booking> findBookingsByOwnerIdAndStatus(Long ownerId);
}

