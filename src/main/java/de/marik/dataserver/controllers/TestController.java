package de.marik.dataserver.controllers;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	// for tesing
//	@GetMapping("/hello")
//	public String test(Model model) {
//		Person person = new Person();
//		person.setUsername("Marik test person");
//		model.addAttribute("person", person);
//		return person.toString();
//	}

	// for tesing
//	@GetMapping("/allexpenses")
//	public String allExpensesTest() {
//		return expensesService.getAllExpenses().toString();
//	}

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
