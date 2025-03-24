package de.marik.apigateway;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DataAccessServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataAccessServerApplication.class, args);
	}
	
	//ModelMapper for fast Model <-> DTO conversion
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

}
