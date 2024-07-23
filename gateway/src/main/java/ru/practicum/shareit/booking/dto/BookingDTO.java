package ru.practicum.shareit.booking.dto;

import ch.qos.logback.core.status.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    @NotNull
    private Long id;
    @NotNull
    private Long itemId;
    private Long bookerId;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    private Status status;
}