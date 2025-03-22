package de.marik.apigateway.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.marik.apigateway.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
	
	// person search by username
	Optional<Person> findByUsername (String username);
}
