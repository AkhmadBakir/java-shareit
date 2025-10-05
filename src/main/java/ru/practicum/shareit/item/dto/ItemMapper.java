package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .request(item.getRequest())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .ownerId(itemDto.getOwnerId())
                .request(itemDto.getRequest())
                .build();
    }

    public static Item newItem(ItemDto itemDto, Long ownerId) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .ownerId(ownerId)
                .request(itemDto.getRequest())
                .build();
    }

    public static void updateItem(Item item, ItemDto itemDto) {
        if (itemDto.hasName()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.hasDescription()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.hasAvailableStatus()) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.hasOwner()) {
            item.setOwnerId(itemDto.getOwnerId());
        }
        if (itemDto.hasRequest()) {
            item.setRequest(itemDto.getRequest());
        }
    }

}