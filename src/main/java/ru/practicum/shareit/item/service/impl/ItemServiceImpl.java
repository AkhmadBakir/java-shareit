package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.AppValidation;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        User owner = userService.getUserById(userId);
        AppValidation.itemValidator(itemDto);
        Item item = ItemMapper.newItem(itemDto, owner);
        Item newItem = itemRepository.save(item);
        log.info("ItemServiceImpl: Добавлена вещь: itemId={}, userId={}", newItem.getId(), owner.getId());
        return ItemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("ItemServiceImpl: вещь с id = " + itemId + " не найдена"));
        if (!userId.equals(item.getOwner().getId())) {
            log.warn("ItemServiceImpl: Пользователь {} не может обновить вещь с id {}", userId, itemId);
            throw new AccessException("изменить вещь может только владелец");
        }
        ItemMapper.updateItem(item, itemDto);
        Item updatedItem = itemRepository.save(item);
        log.info("ItemServiceImpl: Вещь обновлена: itemId={}, userId={}", updatedItem.getId(), userId);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDtoWithDate getItemDtoWithDateById(Long itemId) {
        log.info("ItemServiceImpl: Получение вещи с датами бронирований: itemId={}", itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("ItemServiceImpl: вещь c id = " + itemId + " не найдена"));
        Optional<Booking> nextBooking = bookingRepository
                .findFirstByItemAndStartAfterOrderByStartAsc(item, LocalDateTime.now());
        Optional<Booking> lastBooking = bookingRepository
                .findFirstByItemAndEndAfterOrderByEndDesc(item, LocalDateTime.now());
        List<Comment> comments = commentRepository.findAllByItem(item);
        ItemDtoWithDate itemDtoWithDate = ItemMapper.toItemDtoWithDate(item, comments);
        itemDtoWithDate.setNextBooking(nextBooking.map(Booking::getStart).orElse(null));
        itemDtoWithDate.setLastBooking(lastBooking.map(Booking::getStart).orElse(null));
        log.info("ItemServiceImpl: Вещь найдена: itemId={}", item.getId());
        return itemDtoWithDate;
    }

    @Override
    public List<ItemDtoWithDate> getItemsByUserId(Long userId) {
        User owner = userService.getUserById(userId);
        List<Item> userItems = itemRepository.findAllByOwner(owner);
        List<Booking> bookingsByItemOwner = bookingRepository.findAllByItemOwner(owner);
        LocalDateTime now = LocalDateTime.now();

        List<ItemDtoWithDate> itemsWithDates = userItems.stream()
                .map(item -> {
                    List<Booking> itemBookings = bookingsByItemOwner.stream()
                            .filter(booking -> booking.getItem().getId().equals(item.getId()))
                            .toList();

                    Booking lastBooking = itemBookings.stream()
                            .filter(booking -> booking.getStart().isBefore(now))
                            .max(Comparator.comparing(Booking::getStart))
                            .orElse(null);

                    Booking nextBooking = itemBookings.stream()
                            .filter(booking -> booking.getStart().isAfter(now))
                            .min(Comparator.comparing(Booking::getStart))
                            .orElse(null);

                    return ItemMapper.toItemDtoWithDate(item,
                            lastBooking != null ? lastBooking.getStart() : null,
                            nextBooking != null ? nextBooking.getStart() : null);
                })
                .collect(Collectors.toList());

        log.info("ItemServiceImpl: Получен список вещей пользователя: userId={}, count={}", userId, itemsWithDates.size());
        return itemsWithDates;
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
        log.info("ItemServiceImpl: Вещь удалена: itemId={}", itemId);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        List<Item> searchItem = itemRepository.searchItemsByText(text);
        log.info("ItemServiceImpl: Поиск вещей по тексту '{}', найдено: count={}", text, searchItem.size());
        return searchItem.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("ItemServiceImpl: вещь с id = " + itemId + " не найдена"));
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        log.info("ItemServiceImpl: Добавление комментария: userId={}, itemId={}, text={}", userId, itemId, commentDto.getText());
        User author = userService.getUserById(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("ItemServiceImpl: вещь c id = " + itemId + " не найдена"));

        Booking booking = bookingRepository
                .findFirstByBookerAndItemAndEndBeforeOrderByEndDesc(author, item, LocalDateTime.now())
                .orElseThrow(() -> new ValidationException("бронирование не найдено"));

        if (!booking.getBooker().getId().equals(author.getId())) {
            log.warn("ItemServiceImpl: Пользователь {} не может оставить комментарий к itemId={}", userId, itemId);
            throw new ValidationException("пользователь не может оставить комментарий");
        }
        if (booking.getEnd().isAfter(LocalDateTime.now())) {
            log.warn("ItemServiceImpl: Попытка оставить комментарий до завершения бронирования: userId={}, itemId={}", userId, itemId);
            throw new ValidationException("нельзя оставить комментарий до завершения бронирования");
        }

        Comment comment = CommentMapper.toComment(author, item, commentDto);
        comment.setCreated(LocalDateTime.now());
        Comment newComment = commentRepository.save(comment);

        log.info("ItemServiceImpl: Комментарий добавлен: commentId={}, itemId={}, userId={}", newComment.getId(), itemId, userId);
        return CommentMapper.toCommentDto(newComment);
    }
}