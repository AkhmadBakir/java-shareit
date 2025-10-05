package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto getUserDtoById(Long userId);

    void deleteUser(Long userId);

    List<UserDto> getAllUsers();

    User getUserById(Long userId);

}