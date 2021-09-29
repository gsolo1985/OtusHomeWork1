package ru.otus.currencyAdapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CurrencyAdapterApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyAdapterApplication.class, args);
	}

}
