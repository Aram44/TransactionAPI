package com.example.transactionapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class TransactionapiApplication {



	public static void main(String[] args) {

		SpringApplication.run(TransactionapiApplication.class, args
		);
	}

}
