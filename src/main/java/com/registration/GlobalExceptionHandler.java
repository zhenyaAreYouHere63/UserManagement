package com.registration;

import com.registration.exception.NotEnoughYearsException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorTemplate> handleValidationException(Exception ex) {
        List<ErrorTemplate> errorInfos = new ArrayList<>();
        String title = "Validation exception";

        switch (ex) {
            case MethodArgumentNotValidException mnve -> {
                mnve.getBindingResult().getFieldErrors()
                        .forEach(fe -> errorInfos.add(new ErrorTemplate(title, String.format("[%s: '%s']",fe.getField(), fe.getDefaultMessage()))));
            }
            case ConstraintViolationException cve -> {
                cve.getConstraintViolations()
                        .forEach(v -> errorInfos.add(new ErrorTemplate(title, String.format("[%s: '%s']", v.getPropertyPath().toString(), v.getMessage()))));
            }
            default -> {
            }
        }
        return errorInfos;
    }

    @ExceptionHandler({NotEnoughYearsException.class, NotEnoughYearsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNotEnoughYearsException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
