package ru.practicum.shareit.item.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;


import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@Table(name = "comments", schema = "public")
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "COMMENT_TEXT", nullable = false)
    private String text;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COMMENT_ITEM_ID", nullable = false)
    private Item item;
    @Column(name = "COMMENT_USER_ID", nullable = false)
    private String authorName;
    @Column(name = "COMMENT_CREATED", nullable = false)
    public LocalDateTime created;

}
