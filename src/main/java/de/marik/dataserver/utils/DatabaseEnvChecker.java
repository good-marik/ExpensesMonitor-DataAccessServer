package de.marik.dataserver.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseEnvChecker {

    @Value("${DB_USERNAME}")
    private String dbUsername;

    @PostConstruct
    public void validateDatabaseEnv() {
        if (dbUsername.isEmpty()) {
            System.out.println("---------------------------------------");
        	System.out.println("error in loading env. variables!");
        	System.out.println("---------------------------------------");
        	throw new IllegalStateException("Missing required database environment variables!");
        }
        System.out.println("---------------------------------------");
        System.out.println("Database environment variables are correctly set.");
        System.out.println("---------------------------------------");
    }
}