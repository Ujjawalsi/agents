package com.example.agents;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.agents", "com.vel.common.connector.service","com.vel.common.connector.service.impl"})
public class AgentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgentsApplication.class, args);
	}

}

