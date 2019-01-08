package com.monamitech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Starter {

	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "local");
		SpringApplication.run(Starter.class, args);
	}
}
