package ru.practicum.shareit.booking.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookDto {
    private Long id;
    private Long bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
}
