package de.marik.dataserver;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import de.marik.dataserver.utils.DatabaseEnvChecker;

@SpringBootApplication
public class APIDataServerApplication {
//	@Value("${spring.datasource.url}")
//	String testVariable;

	public static void main(String[] args) {
//		
//		System.out.println("---STARTING SPRING HERE!---");
//		System.out.println("EnvVariable: "+ testVariable);
//		
		ApplicationContext context = SpringApplication.run(APIDataServerApplication.class, args);
		
		context.getBean(DatabaseEnvChecker.class).validateDatabaseEnv();

		
	}

	// ModelMapper for fast Model <-> DTO conversion
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
