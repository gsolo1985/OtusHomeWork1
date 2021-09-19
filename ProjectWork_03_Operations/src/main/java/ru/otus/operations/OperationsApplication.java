package ru.otus.operations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OperationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperationsApplication.class, args);
	}

}
