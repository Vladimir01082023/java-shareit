package ru.practicum.shareit.booking;



public class BookDtoMapper {

    public static BookDto map(Booking book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setBookerId(book.getBooker().getId());
        return bookDto;
    }
}
