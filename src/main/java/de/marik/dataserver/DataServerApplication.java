package de.marik.dataserver;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DataServerApplication {
	
	public static void main(String[] args) {
		System.out.println("Starting DataServer......");
		SpringApplication.run(DataServerApplication.class, args);
	}

	// ModelMapper for fast Model <-> DTO conversion
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
