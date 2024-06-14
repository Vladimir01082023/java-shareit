package ru.practicum.shareit.item;


import lombok.Data;
import ru.practicum.shareit.booking.Booking;


@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
//    private Long requestId;
    private Long ownerId;
    private Booking lastBooking;
    private Booking nextBooking;
}
