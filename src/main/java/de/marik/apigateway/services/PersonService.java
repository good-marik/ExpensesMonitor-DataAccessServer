package de.marik.apigateway.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.marik.apigateway.models.Person;
import de.marik.apigateway.repositories.PersonRepository;

@Service
public class PersonService {

	private final PersonRepository personRepository;

	@Autowired
	public PersonService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	public Optional<Person> getPersonByUsername(String username) {
		return personRepository.findByUsername(username);
	}

}
