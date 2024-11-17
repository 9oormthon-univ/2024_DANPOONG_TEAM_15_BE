package com.ivory.ivory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class IvoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(IvoryApplication.class, args);
	}

}
