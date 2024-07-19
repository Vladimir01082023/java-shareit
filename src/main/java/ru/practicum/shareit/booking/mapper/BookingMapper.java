package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookDto;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


public class BookingMapper {
    public static Booking fromBookerDto(BookingDTO bookingDto, Item item, User user) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }

    public static BookingDTO toBookerDto(Booking booking) {
        BookingDTO bookingDto = new BookingDTO();
        bookingDto.setBookerId(booking.getBooker().getId());
        bookingDto.setItemId(booking.getItem().getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setId(booking.getId());
        return bookingDto;
    }

    public static BookDto map(Booking book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setBookerId(book.getBooker().getId());
        bookDto.setStart(book.getStart());
        bookDto.setEnd(book.getEnd());
        return bookDto;
    }
}
