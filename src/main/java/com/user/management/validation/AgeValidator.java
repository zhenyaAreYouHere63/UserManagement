package com.user.management.validation;

import com.user.management.dto.UserDto;
import com.user.management.exception.AgeLimitException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class AgeValidator implements UserValidator {

    @Value("${user.minAge}")
    private int minAge;

    private LocalDate currentDate = LocalDate.now();

    @Override
    public void validate(UserDto userDto) throws ValidationException {

        int yearsOfUser = (int) userDto.birthday().until(currentDate, ChronoUnit.YEARS);

        if (yearsOfUser <= minAge) {
            throw new AgeLimitException("To register on this site, you must be over " + minAge + " years of age.");
        }
    }
}
