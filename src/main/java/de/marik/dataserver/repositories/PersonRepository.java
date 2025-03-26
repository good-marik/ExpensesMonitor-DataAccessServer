package de.marik.dataserver.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.marik.dataserver.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
	
	// person search by username
	Optional<Person> findByUsername (String username);
}
