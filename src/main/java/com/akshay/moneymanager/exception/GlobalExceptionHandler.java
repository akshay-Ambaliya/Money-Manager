package com.akshay.moneymanager.exception;

import com.akshay.moneymanager.dto.ApiResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Resource Not Found
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex) {

        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .time(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Validation Errors (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(
            MethodArgumentNotValidException ex) {


        ApiResponse response = new ApiResponse();
        response.setStatus( HttpStatus.BAD_REQUEST);
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setTime(LocalDateTime.now());
        response.setData(null);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Constraint Violations
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolation(
            ConstraintViolationException ex) {

        ApiResponse response = new ApiResponse();
        response.setStatus( HttpStatus.BAD_REQUEST);
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setTime(LocalDateTime.now());
        response.setData(null);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Illegal Argument
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgument(
            IllegalArgumentException ex) {

        ApiResponse response = new ApiResponse();
        response.setStatus( HttpStatus.BAD_REQUEST);
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setTime(LocalDateTime.now());
        response.setData(null);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Bad Credential Exception
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgument(
            BadCredentialsException ex) {

        ApiResponse response = new ApiResponse();
        response.setStatus( HttpStatus.BAD_REQUEST);
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setTime(LocalDateTime.now());
        response.setData(null);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    /**
     * Any Other Exception
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception ex) {

        ApiResponse response = new ApiResponse();
        response.setStatus( HttpStatus.BAD_REQUEST);
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setTime(LocalDateTime.now());
        response.setData(null);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}