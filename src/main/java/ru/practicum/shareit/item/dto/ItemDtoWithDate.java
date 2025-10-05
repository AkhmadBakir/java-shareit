package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoWithDate {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private UserDto owner;

    private LocalDateTime lastBooking;

    private LocalDateTime nextBooking;

    private List<CommentDto> comments = new ArrayList<>();

}