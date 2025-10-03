package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.StateParam;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping()
    public ResponseEntity<BookingDto> addBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                 @RequestBody NewBookingRequest newBookingRequest) {
        BookingDto responseBookingDto = bookingService.addBooking(userId, newBookingRequest);
        log.info("BookingController: Бронирование создано: bookingId={}, userId={}", responseBookingDto.getId(), userId);
        return ResponseEntity.ok(responseBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> approvedBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                      @PathVariable(name = "bookingId") Long bookingId,
                                                      @RequestParam(name = "approved") Boolean approved) {
        BookingDto bookingDto = bookingService.approvedBooking(userId, bookingId, approved);
        log.info("BookingController: Статус бронирования обновлён: bookingId={}, approved={}, userId={}", bookingDto.getId(), approved, userId);
        return ResponseEntity.ok(bookingDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBookingByItemOwnerIdOrBookerId(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                                        @PathVariable(name = "bookingId") Long bookingId) {
        BookingDto bookingDto = bookingService.getBookingOnlyByItemOwnerIdOrOnlyByBookerId(userId, bookingId);
        log.info("BookingController: Получено бронирование: bookingId={}, userId={}", bookingDto.getId(), userId);
        return ResponseEntity.ok(bookingDto);
    }

    @GetMapping()
    public ResponseEntity<List<BookingDto>> getBookingsByUserId(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                                @RequestParam(name = "state", required = false) String state) {
        if (state == null) {
            state = "ALL";
        }
        StateParam stateParam = StateParam.getStateParam(state);
        List<BookingDto> bookingDtoList = bookingService.getBookingsByUserId(userId, stateParam);
        log.info("BookingController: Получен список бронирований пользователя: userId={}, state={}, count={}", userId, state, bookingDtoList.size());
        return ResponseEntity.ok(bookingDtoList);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getItemsBookingsByOwnerId(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                                      @RequestParam(name = "state", required = false) String state) {
        if (state == null) {
            state = "ALL";
        }
        StateParam stateParam = StateParam.getStateParam(state);
        List<BookingDto> bookingDtoList = bookingService.getItemsBookingsByOwnerId(userId, stateParam);
        log.info("BookingController: Получен список бронирований владельца: ownerId={}, state={}, count={}", userId, state, bookingDtoList.size());
        return ResponseEntity.ok(bookingDtoList);
    }

}