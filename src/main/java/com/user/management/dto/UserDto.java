package com.user.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

public record UserDto(

        UUID uuid,

        @NotBlank(message = "Field email can't be a blank")
        @Email(message = "Field email invalid")
        String email,

        @NotBlank(message = "Field firstName can't be a blank")
        String firstName,

        @NotBlank(message = "Field lastName can't be a blank")
        String lastName,

        @NotNull(message = "Field birthday can't be a blank")
        @Past(message = "The birthday field must be in the past")
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd"
        )
        LocalDate birthday,

        @Size(max = 1024, message = "The number of characters in the address has been exceeded")
        String address,

        @Size(max = 10, message = "The number of characters in the phone number has been exceeded")
        String phoneNumber
){

}
