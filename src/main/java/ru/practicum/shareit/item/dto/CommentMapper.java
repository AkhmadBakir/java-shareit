package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class CommentMapper {

    public static Comment toComment(User author, Item item, CommentDto commentDto) {
        return Comment.builder()
                .author(author)
                .item(item)
                .text(commentDto.getText())
                .created(commentDto.getCreated())
                .build();
    }

    public static CommentDto toCommentDto(Comment newComment) {
        return CommentDto.builder()
                .id(newComment.getId())
                .authorName(newComment.getAuthor().getName())
                .itemId(newComment.getItem().getId())
                .text(newComment.getText())
                .created(newComment.getCreated())
                .build();
    }

}