package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private UserDto owner;

    private List<CommentDto> comments = new ArrayList<>();

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasAvailableStatus() {
        return available != null;
    }

}