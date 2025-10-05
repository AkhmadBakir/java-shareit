package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        UserDto newUserDto = userService.addUser(userDto);
        log.info("UserController: добавлен новый пользователь с id = {}", newUserDto.getId());
        return ResponseEntity.ok(newUserDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(value = "userId") Long userId,
                                              @RequestBody UserDto userDto) {
        UserDto updateUserDto = userService.updateUser(userId, userDto);
        log.info("UserController: пользователь с id = {} обновлен", updateUserDto.getId());
        return ResponseEntity.ok(updateUserDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "userId") Long userId) {
        UserDto userDto = userService.getUserById(userId);
        log.info("UserController: получение пользователя с id = {}", userDto.getId());
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> allUsers = userService.getAllUsers();
        log.info("UserController: получение списка всех пользователей, количество пользователей = {}", allUsers.size());
        return ResponseEntity.ok(allUsers);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "userId") Long userId) {
        userService.deleteUser(userId);
        log.info("UserController: удаление пользователя с id = {}", userId);
        return ResponseEntity.noContent().build();
    }

}