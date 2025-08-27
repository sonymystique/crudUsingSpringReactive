package com.example.curdUsingMongoDB.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
@Component
public class UserInitializer {
    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Users user1 = new Users();
            user1.setUsername("user1");
            user1.setPassword(passwordEncoder.encode("password1"));
            user1.setRole("USER");

            Users admin = new Users();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password1"));
            admin.setRole("ADMIN");

            Flux.just(user1, admin)
                    .flatMap(userRepository::insert);

            userRepository.deleteAll()
                    .thenMany(Flux.just(user1, admin))
                    .flatMap(userRepository::save)
                    .doOnNext(user -> System.out.println("Inserted user: " + user.getUsername()))
                    .subscribe();


        };
    }
}
