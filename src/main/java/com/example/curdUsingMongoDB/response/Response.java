package com.example.curdUsingMongoDB.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private String message;
    private LocalDateTime timestamp;
    private String status;
}