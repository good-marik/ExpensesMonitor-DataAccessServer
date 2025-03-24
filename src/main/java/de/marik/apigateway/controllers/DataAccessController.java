package de.marik.apigateway.controllers;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.marik.apigateway.models.Expenses;
import de.marik.apigateway.models.Person;
import de.marik.apigateway.services.ExpensesService;
import de.marik.apigateway.services.PersonService;

@RestController
public class DataAccessController {

	final private ExpensesService expensesService;
	final private PersonService personService;

	@Autowired
	public DataAccessController(ExpensesService expensesService, PersonService personService) {
		this.expensesService = expensesService;
		this.personService = personService;
	}

	@GetMapping("/hello")
	public String test(Model model) {
		Person person = new Person();
		person.setUsername("Marik test person");
		model.addAttribute("person", person);
		return person.toString();
	}

	@GetMapping("/allexpenses")
	public String allExpensesTest() {
		return expensesService.getAllExpenses().toString();
	}

	//for testing only
//	@GetMapping("/putitem")
//	public String addNewExpensesTest() {
//		Person person = personService.getPersonByUsername("marik").get();
//		Random random = new Random();
//		Expenses expenses = new Expenses(random.nextDouble(100), "new expenses N" + random.nextInt(1000), person);
//		expensesService.save(expenses);
//		return "The expenses was added";
//	}
}
