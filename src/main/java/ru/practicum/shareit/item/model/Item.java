package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.shareit.request.ItemRequest;

@Setter
@Getter
@Builder
@ToString
@EqualsAndHashCode()
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private Long id;

    @NotBlank
    @NotEmpty
    @NotNull
    private String name;

    @Size(max = 200)
    @NotBlank
    @NotEmpty
    @NotNull
    private String description;

    private Boolean available;

    private Long ownerId;

    private ItemRequest request;

}