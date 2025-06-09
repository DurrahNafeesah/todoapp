package com.example.todoapp.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle @Valid errors like title, description, etc.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle malformed JSON like invalid date format or invalid enum value
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFormat(HttpMessageNotReadableException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());

        InvalidFormatException formatEx = findInvalidFormatException(ex);

        if (formatEx != null) {
            Class<?> targetType = formatEx.getTargetType();


            System.out.println("InvalidFormatException targetType: " + targetType.getName());

            if (LocalDate.class.equals(targetType)) {
                response.put("error", "Due date must be in yyyy-MM-dd'T'HH:mm:ss format");
            } else if (targetType.equals(com.example.todoapp.entity.TaskStatusMaster.class)) {
                response.put("error", "Invalid status value. Allowed values are: NEW, STARTED, DONE, COMPLETED.");
            } else {
                response.put("error", "Malformed JSON request");
            }
        } else {
            response.put("error", "Malformed JSON request");
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());

        if (ex.getMessage() != null && ex.getMessage().contains("Invalid status")) {
            response.put("error", ex.getMessage());
        } else {
            response.put("error", "Invalid input: " + ex.getMessage());
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    private InvalidFormatException findInvalidFormatException(Throwable ex) {
        if (ex == null) {
            return null;
        }
        if (ex instanceof InvalidFormatException) {
            return (InvalidFormatException) ex;
        }
        return findInvalidFormatException(ex.getCause());
    }
}
