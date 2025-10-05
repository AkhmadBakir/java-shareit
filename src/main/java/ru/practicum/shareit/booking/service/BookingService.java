package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.StateParam;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(Long userId, NewBookingRequest bookingDto);

    BookingDto approvedBooking(Long userId, Long bookingId, Boolean approved);

    BookingDto getBookingOnlyByItemOwnerIdOrOnlyByBookerId(Long userId, Long bookingId);

    List<BookingDto> getBookingsByUserId(Long userId, StateParam stateParam);

    List<BookingDto> getItemsBookingsByOwnerId(Long userId, StateParam stateParam);

}