package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestBody ItemDto itemDto) {
        ItemDto newItemDto = itemService.addItem(userId, itemDto);
        log.info("ItemController: Добавлена вещь: itemId={}, userId={}", newItemDto.getId(), userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newItemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable(value = "itemId") Long itemId,
                                              @RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestBody ItemDto itemDto) {
        log.info("ItemController: попытка обновить вещь: itemId={}, userId={}", itemId, userId);
        ItemDto updateItemDto = itemService.updateItem(itemId, userId, itemDto);
        log.info("ItemController: вещь обновлена: itemId={}, userId={}", updateItemDto.getId(), userId);
        return ResponseEntity.ok(updateItemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDtoWithDate> getItemById(@PathVariable(value = "itemId") Long itemId,
                                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemDtoWithDate itemDtoWithDate = itemService.getItemDtoWithDateById(itemId);
        log.info("ItemController: Получение вещи: itemId={}, userId={}", itemId, userId);
        return ResponseEntity.ok(itemDtoWithDate);
    }

    @GetMapping
    public ResponseEntity<List<ItemDtoWithDate>> getItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemDtoWithDate> usersItemDto = itemService.getItemsByUserId(userId);
        log.info("ItemController: Получен список вещей пользователя: userId={}, count={}", userId, usersItemDto.size());
        return ResponseEntity.ok(usersItemDto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable(value = "itemId") Long itemId,
                                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        itemService.deleteItem(itemId);
        log.info("ItemController: Удалена вещь: itemId={}, userId={}", itemId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestParam(value = "text") String text) {
        List<ItemDto> searchItem = itemService.searchItems(text);
        log.info("ItemController: Поиск вещей по тексту '{}', найдено: count={}", text, searchItem.size());
        return ResponseEntity.ok(searchItem);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable(value = "itemId") Long itemId,
                                                 @RequestBody CommentDto text) {
        log.info("ItemController: Добавление комментария: itemId={}, userId={}, text={}", itemId, userId, text.getText());
        CommentDto commentDto = itemService.addComment(userId, itemId, text);
        log.info("ItemController: Комментарий добавлен: commentId={}, itemId={}, userId={}", commentDto.getId(), itemId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
    }
}