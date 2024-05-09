package com.user.management.service;

import com.user.management.dto.UserDto;
import com.user.management.dto.UserUpdateDto;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UUID registerUser(UserDto userDto);

    UserDto updateUserPartially(String id, UserUpdateDto userDto);

    UserDto updateUserAllFields(String id, UserDto userDto);

    void deleteUser(String id);

    List<UserDto> getUsersByBirthdayBetweenFromBirthdayAndToBirthday(LocalDate fromBirthday, LocalDate toBirthday);
}
