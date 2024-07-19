package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item_request", schema = "public")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_REQUEST_ID")
    private Long id;
    @Column(name = "ITEM_REQUEST_DESCRIPTION", nullable = false)
    private String description;
    @Column(name = "REQUESTOR_ID", nullable = false)
    private Long requestor;
    @Column(name = "CREATED", nullable = false)
    private LocalDateTime created;
    @Transient
    private List<ItemResponseDto> items;
}
