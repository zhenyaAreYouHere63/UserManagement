package com.registration.dao;

import com.registration.exception.NotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Getter
@Setter
public class UsersList {

    private List<Users> users;

    public UsersList() {
        this.users = new ArrayList<>();
    }

    public UUID addUser(Users user) {
        users.add(user);
        return user.getUuid();
    }

    public UUID deleteUser(String email) {
        Users usersToRemove = users.stream().filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));

        users.remove(usersToRemove);
        return usersToRemove.getUuid();
    }

    public Users findUserByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
    }

    public List<Users> getUsersByBirthday(LocalDate fromBirthday, LocalDate toBirthday) {
        return users.stream()
                .filter(user -> user.getBirthday().isAfter(fromBirthday) && user.getBirthday().isBefore(toBirthday))
                .toList();
    }
}
