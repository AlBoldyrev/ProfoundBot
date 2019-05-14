package com.vk.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Configuration
@EnableJpaRepositories("com.vk.repository")
@EntityScan(basePackages = "com.vk.entities")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}



