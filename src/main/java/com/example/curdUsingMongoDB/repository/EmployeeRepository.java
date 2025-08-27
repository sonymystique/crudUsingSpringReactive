package com.example.curdUsingMongoDB.repository;

import com.example.curdUsingMongoDB.entity.Employees;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeRepository extends ReactiveMongoRepository<Employees, String> {
    Flux<Employees> findByEmployeeEmailContaining(String domain);
    Flux<Employees> findAllBy(Pageable pageable);
}
