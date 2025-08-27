package com.example.curdUsingMongoDB.controller;

import com.example.curdUsingMongoDB.dto.EmployeesDto;
import com.example.curdUsingMongoDB.response.Response;
import com.example.curdUsingMongoDB.service.EmployeesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/ems")
@AllArgsConstructor
@Tag(name = "Employee APIs", description = "Crud Operations on Employee ")
public class EmployeesController {
    private EmployeesService employeeService;

    @GetMapping("/employees/filterByDomain")
    public ResponseEntity<Flux<EmployeesDto>> getByFilter() {
        return new ResponseEntity<>(employeeService.getFilteredDetails(), HttpStatus.OK);

    }

    @PostMapping("/employee")
    public Mono<ResponseEntity<EmployeesDto>> addEmployee(@Valid @RequestBody EmployeesDto dto) {
        return employeeService.createEmployee(dto)
                .map(createdEmployee -> new ResponseEntity<>(createdEmployee, HttpStatus.CREATED));
    }

    @GetMapping(path = "/employees",params = {"offset", "page"})
    public Mono<Page<EmployeesDto>> getEmployeeByPagination(@RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer page) {

            Pageable pageable = PageRequest.of(offset, page);
            return employeeService.getAllDetailsPage(pageable);

    }

    @GetMapping("/employees")
    public Flux<EmployeesDto> getEmployee() {

        return employeeService.getAllDetails();

    }


    @PutMapping("/employee/{id}")
    public Mono<ResponseEntity<EmployeesDto>> updateEmployeeDetails(@PathVariable String id, @Valid @RequestBody EmployeesDto dto) {
        return employeeService.updateEmployees(id, dto)
                .map(updatedEmployee -> new ResponseEntity<>(updatedEmployee, HttpStatus.OK));
    }

    @DeleteMapping("/employee/{id}")
    public Mono<Response> deleteEmployee(@PathVariable String id) {
        return employeeService.deleteEmployees(id);
    }

}