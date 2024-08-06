package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFromClient;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


public class BookingMapper {
    public static Booking fromBookerDto(BookingDtoFromClient bookingDto, Item item, User user) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }

    public static BookingDtoFromClient toBookerDtoFromClient(Booking booking) {
        BookingDtoFromClient bookingDto = new BookingDtoFromClient();
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

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setItem(booking.getItem());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setId(booking.getId());
        return bookingDto;
    }
}