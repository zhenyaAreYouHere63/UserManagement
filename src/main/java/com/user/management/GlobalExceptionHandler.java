package com.user.management;

import com.user.management.exception.AgeLimitException;
import com.user.management.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(AgeLimitException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNotEnoughYearsException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
