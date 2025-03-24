package de.marik.apigateway.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.marik.apigateway.dto.ExpensesDTO;
import de.marik.apigateway.models.Expenses;
import de.marik.apigateway.models.Person;
import de.marik.apigateway.repositories.ExpensesRepository;
import de.marik.apigateway.repositories.PersonRepository;

@Service
@Transactional(readOnly = true)
public class ExpensesService {

	private final ExpensesRepository expensesRepository;
	private final PersonRepository personRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public ExpensesService(ExpensesRepository expensesRepository, PersonRepository personRepository,
			ModelMapper modelMapper) {
		this.expensesRepository = expensesRepository;
		this.personRepository = personRepository;
		this.modelMapper = modelMapper;
	}

	public List<Expenses> getAllExpenses() {
		return expensesRepository.findAll();
	}

	@Transactional
	public void saveExpenses(ExpensesDTO expensesDTO) {
//		System.out.println("ExpensesDTO:");
//		System.out.println(expensesDTO);
		Expenses expenses = modelMapper.map(expensesDTO, Expenses.class);
		Optional<Person> person = personRepository.findById(expensesDTO.getOwnerIdentity());
//		System.out.println("Person:");
//		System.out.println(person);
//		System.out.println("Expenses:");
//		System.out.println(expenses);
		if(person.isEmpty()) {
			//debugging HERE, this must be checked in controller
			System.out.println("error in writing in DB");
			return;
		}
		expenses.setOwner(person.get());
//		System.out.println("saving entry in DB...");
//		System.out.println(expenses);
		expensesRepository.save(expenses);
//		System.out.println("saved successfully in DB!");
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
