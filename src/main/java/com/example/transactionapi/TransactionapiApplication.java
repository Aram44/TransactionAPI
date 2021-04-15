package com.example.transactionapi;

import com.example.transactionapi.models.Role;
import com.example.transactionapi.services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class TransactionapiApplication {

	@Autowired
	private ShowService<Role> roleService;

	@Bean
	public WebMvcConfigurer crosConfigurer(){
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/*").allowedHeaders("*").allowedOrigins("*")
						.allowedMethods("*").allowCredentials(true);
			}
		};
	}

	public static void main(String[] args) {

		SpringApplication.run(TransactionapiApplication.class, args
		);
	}

}
