package com.user.management.controller;

import com.user.management.dao.User;
import com.user.management.dto.UserDto;
import com.user.management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public java.util.UUID registerUser(@RequestBody @Valid UserDto userDto) {
        return userService.registerUser(userDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User partialUpdateUser(@PathVariable @UUID String id,
                                  @RequestBody @Valid UserDto userDto) {
        return userService.partialUpdateUser(id, userDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUserAllFields(@PathVariable @UUID String id,
                                    @RequestBody @Valid UserDto userDto) {
        return userService.updateUserAllFields(id, userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @UUID String id) {
        userService.deleteUser(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsersByBirthdayBetweenFromBirthdayAndToBirthday(@RequestParam @Valid LocalDate fromBirthday,
                                                                         @RequestParam @Valid LocalDate toBirthday) {
        return userService.getUsersByBirthdayBetweenFromBirthdayAndToBirthday(fromBirthday, toBirthday);
    }
}
