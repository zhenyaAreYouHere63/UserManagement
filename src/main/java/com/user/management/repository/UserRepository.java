package com.user.management.repository;

import com.user.management.dao.User;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface UserRepository<T> {

    UUID saveUser(T entity);

    void deleteUser(UUID uuid);

    User findUserByUuid(UUID uuid);

    List<User> getUsersByBirthdayBetweenFromBirthdayAndToBirthday(LocalDate fromBirthday, LocalDate toBirthday);
}
