package com.user.management.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.user.management.dao.User;
import com.user.management.exception.NotFoundException;

@Component
public class UserRepositoryImpl implements UserRepository<User> {

    @Override
    public UUID saveUser(User user) {
        DataStorage.users.add(user);
        return user.getUuid();
    }

    @Override
    public void deleteUser(UUID uuid) {
        DataStorage.users.stream()
                .filter(user -> user.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User with uuid " + uuid + " not found"));
    }

    @Override
    public User findUserByUuid(UUID uuid) {
        return DataStorage.users.stream()
                .filter(user -> user.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User with uuid " + uuid + " not found"));
    }

    @Override
    public List<User> getUsersByBirthdayBetweenFromBirthdayAndToBirthday(LocalDate fromBirthday, LocalDate toBirthday) {
        return DataStorage.users.stream()
                .filter(user -> user.getBirthday().isAfter(fromBirthday)
                                && user.getBirthday().isBefore(toBirthday))
                .toList();
    }
}
