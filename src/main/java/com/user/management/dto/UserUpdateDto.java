package com.user.management.dto;

import com.user.management.validation.MinAgeValidation;
import java.time.LocalDate;
import java.util.UUID;

public record UserUpdateDto(

        UUID uuid,

        String email,

        String firstName,

        String lastName,

        @MinAgeValidation
        LocalDate birthday,

        String address,

        String phoneNumber
) {
}
