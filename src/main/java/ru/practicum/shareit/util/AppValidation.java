package ru.practicum.shareit.util;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@UtilityClass
public final class AppValidation {

    public static void userValidator(UserDto userDto) {
        if (userDto.getEmail() == null ||
                userDto.getEmail().isBlank() ||
                !userDto.getEmail().contains("@")) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
    }

    public static void itemValidator(ItemDto itemDto) {
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("в запросе отсутствует поле доступности к бронированию");
        }
        if (itemDto.getName() == null ||
                itemDto.getName().isBlank()) {
            throw new ValidationException("в запросе поле 'name' отсутствует или не задано");
        }
        if (itemDto.getDescription() == null ||
                itemDto.getDescription().isBlank()) {
            throw new ValidationException("в запросе поле 'name' отсутствует или не задано");
        }
    }

    public static void bookingValidation(NewBookingRequest newBookingRequest) {
        if (newBookingRequest.getItemId() == null) {
            throw new ValidationException("в запросе поле 'itemId' отсутствует или не задано");
        }
        if (newBookingRequest.getStart() == null || newBookingRequest.getEnd() == null) {
            throw new ValidationException("даты начала и конца бронирования должны быть указаны");
        }
        if (newBookingRequest.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("дата конца бронирования не может быть в прошлом");
        }
        if (newBookingRequest.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("дата начала бронирования не может быть в прошлом");
        }
        if (newBookingRequest.getEnd().equals(newBookingRequest.getStart())) {
            throw new ValidationException("даты начала и конца бронирования не могут быть одинаковыми");
        }
    }

}