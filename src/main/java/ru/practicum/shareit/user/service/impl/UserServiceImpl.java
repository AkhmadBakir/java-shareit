package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.AppValidation;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {
        AppValidation.userValidator(userDto);
        User user = userRepository.save(UserMapper.newUser(userDto));
        log.info("UserServiceImpl: добавлен новый пользователь с id = {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("UserServiceImpl: пользователь с id = " + userId + " не найден"));
        UserMapper.updateUser(user, userDto);
        User updateUser = userRepository.save(user);
        log.info("UserServiceImpl: пользователь с id = {} обновлен", updateUser.getId());
        return UserMapper.toUserDto(updateUser);
    }

    @Override
    public UserDto getUserDtoById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("UserServiceImpl: пользователь с id = " + userId + " не найден"));
        log.info("UserServiceImpl: получение пользователя с id = {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> allUsers = userRepository.findAll(); //TODO переделать с использованием пагинации
        log.info("UserServiceImpl: получение списка всех пользователей, количество пользователей = {}", allUsers.size());
        return allUsers.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("UserServiceImpl: пользователь с id = " + userId + " не найден"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        log.info("UserServiceImpl: удаление пользователя с id = {}", userId);
    }

}