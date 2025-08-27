package com.example.curdUsingMongoDB.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeesDto {

    @NotEmpty(message = "Full name is required")
    @Schema(description = "Employee's fullname")
    private String fullName;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    @Schema(description = "Employee's email address")
    private String email;

    private LocalDateTime createdTimeStamp;

    private LocalDateTime lastModifiedTimeStamp;



}

