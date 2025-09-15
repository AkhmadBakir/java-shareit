package ru.practicum.shareit.util;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

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

}