package com.example.curdUsingMongoDB.service;

import com.example.curdUsingMongoDB.dto.EmployeesDto;
import com.example.curdUsingMongoDB.entity.Employees;
import com.example.curdUsingMongoDB.response.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeesService {
    public Flux<EmployeesDto> getAllDetails();
    public Flux<EmployeesDto> getFilteredDetails();
    public Mono<EmployeesDto> createEmployee(EmployeesDto dto);
    public Mono<EmployeesDto> updateEmployees(String id, EmployeesDto employeeDTO);
    public Mono<Response> deleteEmployees(String id);
    public Mono<Page<EmployeesDto>> getAllDetailsPage(Pageable pageable);

}
