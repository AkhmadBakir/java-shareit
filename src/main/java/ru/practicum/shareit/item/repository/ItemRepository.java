package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item addItem(Item item, Long ownerId);

    Item getItemById(Long itemId);

    List<Item> getItemsByUserId(Long userId);

    void updateItem(Item item);

    List<Item> getAllItems();

    void deleteItem(Long itemId);

    List<Item> searchItems(String text);

}