package de.marik.apigateway.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.marik.apigateway.models.Person;
import de.marik.apigateway.repositories.PersonRepository;

@Service
@Transactional(readOnly = true)
public class PersonService {

	private final PersonRepository personRepository;

	@Autowired
	public PersonService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	public Optional<Person> getPersonByUsername(String username) {
		return personRepository.findByUsername(username);
	}

	//for tests only
	public List<Person> getAllPeople() {
		return personRepository.findAll();
	}

}
