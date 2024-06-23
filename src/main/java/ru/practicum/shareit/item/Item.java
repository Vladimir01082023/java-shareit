package ru.practicum.shareit.item;


import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@AllArgsConstructor
@Entity
@Table(name = "items", schema = "public")
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    @Column(name = "item_name", nullable = false)
    private String name;
    @Column(name = "item_description", nullable = false)
    private String description;
    @Column(name = "item_available", nullable = false)
    private Boolean available;
    //    @Column(name = "item_request_id", nullable = false)
//    private Long requestId;
    @Column(name = "item_owner_id", nullable = false)
    private Long ownerId;

}
