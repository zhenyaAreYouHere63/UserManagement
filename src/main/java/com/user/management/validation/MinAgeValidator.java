package com.user.management.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MinAgeValidator implements ConstraintValidator<MinAgeValidation, LocalDate> {

    @Value("${user.minAge}")
    private int minAge;

    private String message;

    @Override
    public void initialize(MinAgeValidation constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }

        LocalDate currentDate = LocalDate.now();
        int yearsOfUser = (int) value.until(currentDate, ChronoUnit.YEARS);

        if (yearsOfUser <= minAge) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message.replace("{value}", String.valueOf(minAge)))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
