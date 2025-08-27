package com.example.curdUsingMongoDB.service.impl;

import com.example.curdUsingMongoDB.dto.EmployeesDto;
import com.example.curdUsingMongoDB.entity.Employees;
import com.example.curdUsingMongoDB.exceptions.DomainNotFoundException;
import com.example.curdUsingMongoDB.exceptions.EmployeeNotFoundException;
import com.example.curdUsingMongoDB.exceptions.InvalidInputException;
import com.example.curdUsingMongoDB.mappers.EmployeesMapper;
import com.example.curdUsingMongoDB.repository.EmployeeRepository;
import com.example.curdUsingMongoDB.response.Response;
import com.example.curdUsingMongoDB.service.EmployeesService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
    @Service
    public class EmployeesServiceImpl implements EmployeesService {
        @Autowired
        private final EmployeeRepository employeesRepository;
        @Autowired
        private final EmployeesMapper employeesMapper;

        public static final String domain = "@mycompany.com";

        public EmployeesServiceImpl(EmployeeRepository employeesRepository, EmployeesMapper employeesMapper) {
            this.employeesRepository = employeesRepository;
            this.employeesMapper = employeesMapper;
        }


        @Override
        public Flux<EmployeesDto> getAllDetails() {
            return employeesRepository.findAll()
                    .switchIfEmpty(Mono.error(new EmployeeNotFoundException("Database is empty")))
                    .map(employeesMapper::toDTO)
                    .onErrorResume(EmployeeNotFoundException.class, ex -> {
                        return Mono.error(new EmployeeNotFoundException("Add some data first"));
                    });
        }

        @Override
        public Mono<Page<EmployeesDto>> getAllDetailsPage(Pageable pageable){
            return employeesRepository.findAllBy(pageable)
                    .collectList()
                    .zipWith(employeesRepository.count())
                    .map(tuple ->{
                        List<Employees> employeesList = tuple.getT1();
                        Long totalCount = tuple.getT2();

                        List<EmployeesDto> employeesDtos = employeesList.stream()
                                .map(employeesMapper::toDTO)
                                .collect(Collectors.toList());
                        return new PageImpl<>(employeesDtos,pageable,totalCount);
                    });


        }

        @Override
        public Flux<EmployeesDto> getFilteredDetails() {
            return employeesRepository.findByEmployeeEmailContaining(domain)
                    .map(employeesMapper::toDTO)
                    .switchIfEmpty(Flux.error(new DomainNotFoundException("No employee found with the given domain: " + domain)))
                    .onErrorResume(DomainNotFoundException.class, ex -> {
                        return Flux.error(new DomainNotFoundException("No employee found with the given domain"));
                    });
        }

        @Override
        public Mono<EmployeesDto> createEmployee(EmployeesDto dto) {
            return Mono.justOrEmpty(dto)
                    .switchIfEmpty(Mono.error(new InvalidInputException("EmployeeDTO cannot be null")))
                    .map(employeesMapper::toEntity)
                    .flatMap(employeesRepository::save)
                    .map(employeesMapper::toDTO)
                    .onErrorResume(DataIntegrityViolationException.class, ex -> Mono.error(new DataIntegrityViolationException("Duplicated values")))
                    .onErrorResume(HttpMessageNotReadableException.class, ex -> Mono.error(new HttpMessageNotReadableException("Input cannot be converted to JSON")))
                    .onErrorResume(InvalidInputException.class, ex -> {
                        log.error("Error in input: {}", ex.getMessage());
                        return Mono.error(new InvalidInputException("Not able to create new employee"));
                    });
        }

        @Override
        public Mono<EmployeesDto> updateEmployees(String id, EmployeesDto employeeDTO) {
            return employeesRepository.findById(id)
                    .switchIfEmpty(Mono.error(new EmployeeNotFoundException("No employee with given ID")))
                    .flatMap(employees -> {
                        employees.setName(employeeDTO.getFullName());
                        employees.setEmployeeEmail(employeeDTO.getEmail());
                        return employeesRepository.save(employees);
                    })
                    .map(employeesMapper::toDTO)
                    .onErrorResume(EmployeeNotFoundException.class, ex -> Mono.error(new EmployeeNotFoundException("No employee with given ID")));
        }

        @Override
        public Mono<Response> deleteEmployees(String id) {
            return employeesRepository.findById(id)
                    .switchIfEmpty(Mono.error(new EmployeeNotFoundException("No employee with given ID")))
                    .flatMap(employee -> employeesRepository.deleteById(id)
                            .then(Mono.fromSupplier(() -> {
                                Response response = new Response();
                                response.setMessage("the item has been deleted!");
                                response.setTimestamp(LocalDateTime.now());
                                response.setStatus(String.valueOf(HttpStatus.OK.value()));
                                return response;
                            }))
                    );
        }

    }

