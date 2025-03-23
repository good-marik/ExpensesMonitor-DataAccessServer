package de.marik.apigateway.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.marik.apigateway.models.Expenses;
import de.marik.apigateway.models.Person;
import de.marik.apigateway.repositories.ExpensesRepository;
import de.marik.apigateway.repositories.PersonRepository;

@Service
@Transactional(readOnly = true)
public class ExpensesService {

	private final ExpensesRepository expensesRepository;
	private final PersonRepository personRepository;

	@Autowired
	public ExpensesService(ExpensesRepository expensesRepository, PersonRepository personRepository) {
		this.expensesRepository = expensesRepository;
		this.personRepository = personRepository;
	}

	public List<Expenses> getAllExpenses() {
		return expensesRepository.findAll();
	}

	@Transactional
	public void save(Expenses expenses) {
		//debugging
		expenses.setOwner(personRepository.getReferenceById(1));
		System.out.println("saving entry in DB...");
		System.out.println(expenses.toString());
		expensesRepository.save(expenses);
		System.out.println("saved successfully in DB!");
	}

	public List<Expenses> getExpensesByOwnerID(int id) {
		Optional<Person> person = personRepository.findById(id);
		if (person.isEmpty()) {
			System.out.println("Person not found ERROR!");
			return Collections.emptyList();
		} else {
//			Hibernate.initialize(person.getBooks()); // optional
			//System.out.println("Person: " + person.get().getUsername());
			return person.get().getExpenses();
		}
	}
}
