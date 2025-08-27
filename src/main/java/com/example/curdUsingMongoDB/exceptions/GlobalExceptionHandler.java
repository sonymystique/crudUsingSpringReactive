package com.example.curdUsingMongoDB.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {DomainNotFoundException.class, EmployeeNotFoundException.class})
    public ResponseEntity<Map<String, Object>>
    handleEmailDomainNotFound(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidArgument(InvalidInputException ex) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("timestamp", LocalDateTime.now());
        errorMap.put("status", HttpStatus.BAD_REQUEST.value());
        errorMap.put("error", "Not Found");
        errorMap.put("message", ex.getMessage());
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }
    //pending for testing
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> methodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String,Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "Not Found");
        body.put("status",HttpStatus.BAD_REQUEST.value());
        ex.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName =((FieldError)error).getField();
            String errorMessage= error.getDefaultMessage();
            body.put(fieldName,errorMessage);
        });
        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> uniqueContraintViolation(DataIntegrityViolationException ex) {
        Map<String,Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "Duplicated values");
        body.put("status",HttpStatus.BAD_REQUEST.value());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<Map<String, Object>> HttpMessageNotReadable(HttpMessageNotReadableException ex) {
//        Map<String, Object> errorMap = new HashMap<>();
//        errorMap.put("timestamp", LocalDateTime.now());
//        errorMap.put("status", HttpStatus.BAD_REQUEST.value());
//        errorMap.put("error", "Format error");
//        errorMap.put("message", ex.getMessage());
//        return new ResponseEntity<>(errorMap,HttpStatus.BAD_REQUEST);
//    }

}
