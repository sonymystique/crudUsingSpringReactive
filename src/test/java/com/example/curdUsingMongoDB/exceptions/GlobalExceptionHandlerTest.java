package com.example.curdUsingMongoDB.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.xml.validation.Validator;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {
    private Validator validator;
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;
    @Autowired
    private MockMvc mockMvc;


    @Test
    void testemployeeNotFoundException(){
        EmployeeNotFoundException ex = new EmployeeNotFoundException("employee not found");
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleEmailDomainNotFound(ex);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals("employee not found", response.getBody().get("message"));
    }
    @Test
    void testInvalidInputException(){
        InvalidInputException ex = new InvalidInputException("invalid input");
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleInvalidArgument(ex);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals("invalid input", response.getBody().get("message"));
    }

    @Test
    void testDomainNotFoundException(){
        DomainNotFoundException ex = new DomainNotFoundException("specific domain not found");
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleEmailDomainNotFound(ex);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals("specific domain not found", response.getBody().get("message"));

    }
    @Test
    void testMethodArgumentNotValidException() {
        FieldError fieldError = new FieldError("employeeDTO", "fullName", "must not be null");

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.methodArgumentNotValid(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());


    }
    @Test
    void testDataIntegrityViolationException(){
        DataIntegrityViolationException ex = new DataIntegrityViolationException("duplicated values");
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.uniqueContraintViolation(ex);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals("duplicated values", response.getBody().get("message"));

    }

//    @Test
//    void testHttpMessageNotReadableException(){
//        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("empty response");
//        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.HttpMessageNotReadable(ex);
//        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
//        assertEquals("empty response", response.getBody().get("message"));
//    }

}
