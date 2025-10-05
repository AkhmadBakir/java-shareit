package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto);

    ItemDtoWithDate getItemDtoWithDateById(Long itemId);

    List<ItemDtoWithDate> getItemsByUserId(Long userId);

    void deleteItem(Long itemId);

    List<ItemDto> searchItems(String text);

    Item getItemById(Long itemId);

    CommentDto addComment(Long userId, Long itemId, CommentDto text);

}