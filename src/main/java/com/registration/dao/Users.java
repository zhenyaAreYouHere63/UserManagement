package com.registration.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import static com.registration.validation.ValidationErrorMessages.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    private UUID uuid;

    @NotBlank(message = NOT_BLANK_MESSAGE)
    private String email;

    @NotBlank(message = NOT_BLANK_MESSAGE)
    private String firstName;

    @NotBlank(message = NOT_BLANK_MESSAGE)
    private String lastName;

    @NotNull(message = NOT_NULL_MESSAGE)
    private int age;

    @NotNull(message = NOT_NULL_MESSAGE)
    @Past(message = PAST_MESSAGE)
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd"
    )
    private LocalDate birthday;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(uuid, users.uuid) && Objects.equals(email, users.email) && Objects.equals(firstName, users.firstName) && Objects.equals(lastName, users.lastName) && Objects.equals(birthday, users.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, email, firstName, lastName, birthday);
    }
}
