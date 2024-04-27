package com.registration.service;

import com.registration.dao.Users;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UUID registerUser(Users user);
    Users updateUserEmail(String email, String updateEmail);

    Users updateUserAllFields(Users users);

    UUID deleteUser(String email);

    List<Users> getUsersByBirthday(LocalDate fromBirthday, LocalDate toBirthday);
}
