package com.registration.service;

import com.registration.dao.Users;
import com.registration.dao.UsersList;
import com.registration.exception.NotEnoughYearsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersList users;

    @Value("${user.minAge}")
    private int minAge;

    @Override
    public UUID registerUser(Users user) {
        if (user.getAge() <= minAge) {
            throw new NotEnoughYearsException("To register on this site, you must be over 18 years of age.");
        }

        user =  new Users(UUID.randomUUID(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getAge(), user.getBirthday());
        return users.addUser(user);
    }

    @Override
    public Users updateUserEmail(String email, String updateEmail) {
        Users userByEmail = users.findUserByEmail(email);

        userByEmail.setEmail(updateEmail);

        return userByEmail;
    }

    @Override
    public Users updateUserAllFields(Users user) {
        if (user.getAge() <= minAge) {
            throw new NotEnoughYearsException("To register on this site, you must be over 18 years of age.");
        }

        Users userByEmail = users.findUserByEmail(user.getEmail());

        userByEmail.setFirstName(user.getFirstName());
        userByEmail.setLastName(user.getLastName());
        userByEmail.setAge(user.getAge());
        userByEmail.setBirthday(user.getBirthday());

        return userByEmail;
    }

    @Override
    public UUID deleteUser(String email) {
        return users.deleteUser(email);
    }

    @Override
    public List<Users> getUsersByBirthday(LocalDate fromBirthday, LocalDate toBirthday) {
        return users.getUsersByBirthday(fromBirthday, toBirthday);
    }
}
