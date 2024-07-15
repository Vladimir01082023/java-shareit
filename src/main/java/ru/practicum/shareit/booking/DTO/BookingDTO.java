package ru.practicum.shareit.booking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Status;


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
