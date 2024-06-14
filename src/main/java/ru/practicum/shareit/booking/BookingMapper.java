package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.Optional;


public class BookingMapper {
    public static Booking fromBookerDto(BookingDTO bookingDto, Item item, User user) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }

    public static BookingDTO toBookerDto(Optional<Booking> booking) {
        BookingDTO bookingDto = new BookingDTO();
        bookingDto.setBookerId(booking.get().getBooker().getId());
        bookingDto.setItemId(booking.get().getItem().getId());
        bookingDto.setStart(booking.get().getStart());
        bookingDto.setEnd(booking.get().getEnd());
        return bookingDto;
    }
    public static BookingDTO toBookerDto(Booking booking){
        BookingDTO bookingDto = new BookingDTO();
        bookingDto.setBookerId(booking.getBooker().getId());
        bookingDto.setItemId(booking.getItem().getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setId(booking.getId());
        return bookingDto;
    }
}
