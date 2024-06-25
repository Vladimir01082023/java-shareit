package ru.practicum.shareit.booking.Mapper;


import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.DTO.BookDto;

public class BookDtoMapper {

    public static BookDto map(Booking book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setBookerId(book.getBooker().getId());
        bookDto.setStart(book.getStart());
        bookDto.setEnd(book.getEnd());
        return bookDto;
    }
}
