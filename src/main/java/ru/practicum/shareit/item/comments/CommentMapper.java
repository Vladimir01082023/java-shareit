package ru.practicum.shareit.item.comments;


import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static CommentDto toDTO(Comment comment) {
        CommentDto commentDto = new CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthorName().getName());
        commentDto.setCreated(comment.getCreated());

        return commentDto;
    }

    public static Comment fromDTO(CommentDto dto, Item item, User user) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setId(comment.getId());
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setAuthorName(user);
        return comment;
    }
}
