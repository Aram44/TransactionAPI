package com.example.transactionapi;

import com.example.transactionapi.models.Role;
import com.example.transactionapi.services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class TransactionapiApplication {

	@Autowired
	private ShowService<Role> roleService;

	public static void main(String[] args) {

		SpringApplication.run(TransactionapiApplication.class, args
		);
	}

}
