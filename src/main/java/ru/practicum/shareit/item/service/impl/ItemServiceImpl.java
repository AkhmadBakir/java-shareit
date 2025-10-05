package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.AppValidation;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        User owner = UserMapper.toUser(userService.getUserById(userId));
        AppValidation.itemValidator(itemDto);
        Item item = ItemMapper.newItem(itemDto, owner.getId());
        Item newItem = itemRepository.addItem(item, owner.getId());
        log.info("ItemServiceImpl: вещь c id = {} добавлена пользователю с id = {}", newItem.getId(), owner.getId());
        return ItemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto) {
        Item item = itemRepository.getItemById(itemId);
        if (!userId.equals(item.getOwnerId())) {
            throw new AccessException("изменить вещь может только владелец");
        }
        ItemMapper.updateItem(item, itemDto);
        itemRepository.updateItem(item);
        log.info("ItemServiceImpl: вещь c id = {} пользователя с id = {} обновлена", item.getId(), userId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.getItemById(itemId);
        log.info("ItemServiceImpl: получение вещи c id = {} ", itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByUserId(Long userId) {
        List<Item> userItems = itemRepository.getItemsByUserId(userId);
        log.info("ItemServiceImpl: получение списка вещей пользователя с id = {}", userId);
        return userItems.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAllItems() {
        List<Item> allItems = itemRepository.getAllItems();
        log.info("ItemServiceImpl: получение списка всех вещей, всего {} вещей", allItems.size());
        return allItems.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteItem(itemId);
        log.info("ItemServiceImpl: удаление вещи c id = {}", itemId);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        List<Item> searchItem = itemRepository.searchItems(text);
        log.info("ItemServiceImpl: поиск вещей, в названии или описании которого встречается {}", text);
        return searchItem.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

}