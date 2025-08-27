package com.example.curdUsingMongoDB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@SpringBootApplication
@EnableReactiveMongoAuditing
public class CurdUsingMongoDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurdUsingMongoDbApplication.class, args);
	}
}

