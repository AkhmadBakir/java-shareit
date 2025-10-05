package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.StateParam;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.AppValidation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public BookingDto addBooking(Long userId, NewBookingRequest newBookingRequest) {
        User booker = userService.getUserById(userId);
        AppValidation.bookingValidation(newBookingRequest);
        Item item = itemService.getItemById(newBookingRequest.getItemId());
        if (!item.getAvailable()) {
            throw new ValidationException("на данный момент вещь не доступна для бронирования");
        }
        Booking booking = BookingMapper.toBooking(booker, item, newBookingRequest);
        Booking newBooking = bookingRepository.save(booking);
        BookingDto bookingDto = BookingMapper.toBookingDto(newBooking);
        log.info("BookingServiceImpl: Создано бронирование: bookingId={}, bookerId={}, itemId={}",
                bookingDto.getId(), userId, item.getId());
        return bookingDto;
    }

    @Override
    public BookingDto approvedBooking(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("бронирование не найдено"));
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new AccessException("только собственник вещи может изменять статус");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking savedBooking = bookingRepository.save(booking);
        BookingDto bookingDto = BookingMapper.toBookingDto(savedBooking);
        log.info("BookingServiceImpl: Статус бронирования обновлён: bookingId={}, approved={}, ownerId={}",
                bookingDto.getId(), approved, userId);
        return bookingDto;
    }

    @Override
    public BookingDto getBookingOnlyByItemOwnerIdOrOnlyByBookerId(Long userId, Long bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId).orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        if (!booking.getItem().getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            throw new AccessException("посмотреть бронирование может только владелец вещи или автор бронирования");
        }
        BookingDto bookingDto = BookingMapper.toBookingDto(booking);
        log.info("BookingServiceImpl: Получено бронирование: bookingId={}, userId={}", bookingDto.getId(), userId);
        return bookingDto;
    }

    @Override
    public List<BookingDto> getBookingsByUserId(Long userId, StateParam stateParam) {
        User user = userService.getUserById(userId);
        List<Booking> allUserBookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        switch (stateParam) {
            case ALL -> allUserBookings = bookingRepository.findAllByBooker(user);
            case CURRENT ->
                    allUserBookings = bookingRepository.findAllByBookerAndStartBeforeAndEndAfter(user, now, now);
            case PAST -> allUserBookings = bookingRepository.findAllByBookerAndEndBefore(user, now);
            case FUTURE -> allUserBookings = bookingRepository.findAllByBookerAndStartAfter(user, now);
            case WAITING -> allUserBookings = bookingRepository.getWaitingBookingsByBookerId(userId);
            case REJECTED -> allUserBookings = bookingRepository.getRejectedBookingsByBookerId(userId);
        }
        List<BookingDto> bookingDtoList = allUserBookings.stream().map(BookingMapper::toBookingDto).toList();
        log.info("BookingServiceImpl: Получен список бронирований пользователя: userId={}, state={}, count={}",
                userId, stateParam, bookingDtoList.size());
        return bookingDtoList;
    }

    @Override
    public List<BookingDto> getItemsBookingsByOwnerId(Long userId, StateParam stateParam) {
        User user = userService.getUserById(userId);
        List<Booking> allItemsBookingsByOwnerId = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        switch (stateParam) {
            case ALL -> allItemsBookingsByOwnerId = bookingRepository.findAllByItemOwner(user);
            case CURRENT ->
                    allItemsBookingsByOwnerId = bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(user, now, now);
            case PAST -> allItemsBookingsByOwnerId = bookingRepository.findAllByItemOwnerAndEndBefore(user, now);
            case FUTURE -> allItemsBookingsByOwnerId = bookingRepository.findAllByItemOwnerAndStartAfter(user, now);
            case WAITING -> allItemsBookingsByOwnerId = bookingRepository.getWaitingBookingsByItemOwner(userId);
            case REJECTED -> allItemsBookingsByOwnerId = bookingRepository.getRejectedBookingsByItemOwner(userId);
        }
        List<BookingDto> bookingDtoList = allItemsBookingsByOwnerId.stream().map(BookingMapper::toBookingDto).toList();
        log.info("BookingServiceImpl: Получен список бронирований владельца: ownerId={}, state={}, count={}",
                userId, stateParam, bookingDtoList.size());
        return bookingDtoList;
    }
}