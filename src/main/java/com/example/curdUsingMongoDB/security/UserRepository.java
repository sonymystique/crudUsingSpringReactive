package com.example.curdUsingMongoDB.security;

import com.example.curdUsingMongoDB.entity.Employees;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
@Repository
public interface UserRepository extends ReactiveMongoRepository<Users,String> {

    Mono<Users> findByUsername(String username);
    Mono<Users> insert(Users users);

}
