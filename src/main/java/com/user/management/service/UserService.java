package com.user.management.service;

import com.user.management.dao.User;
import com.user.management.dto.UserDto;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UUID registerUser(UserDto userDto);

    User partialUpdateUser(String id, UserDto userDto);

    User updateUserAllFields(String id, UserDto userDto);

    void deleteUser(String id);

    List<User> getUsersByBirthdayBetweenFromBirthdayAndToBirthday(LocalDate fromBirthday, LocalDate toBirthday);
}
