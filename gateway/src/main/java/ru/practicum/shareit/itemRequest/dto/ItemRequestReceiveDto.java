package ru.practicum.shareit.itemRequest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestReceiveDto {
    private Long id;
    private String description;
}

