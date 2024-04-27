package com.registration.controller;

import com.registration.dao.Users;
import com.registration.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID registerUser(@RequestBody @Valid Users user) {
        return userService.registerUser(user);
    }

    @PutMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Users updateUserEmail(@PathVariable @Valid String email,
                                 @RequestBody @Valid String updateEmail) {
        return userService.updateUserEmail(email, updateEmail);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Users updateUserAllFields(@RequestBody @Valid Users user) {
        return userService.updateUserAllFields(user);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public UUID deleteUser(@PathVariable @Valid String email) {
        return userService.deleteUser(email);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Users> getUsersByBirthday(@RequestParam @Valid LocalDate fromBirthday,
                                          @RequestParam @Valid LocalDate toBirthday) {
        return userService.getUsersByBirthday(fromBirthday, toBirthday);
    }
}
