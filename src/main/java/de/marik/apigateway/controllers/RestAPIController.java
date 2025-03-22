package de.marik.apigateway.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.marik.apigateway.models.Expenses;
import de.marik.apigateway.services.ExpensesService;

@RequestMapping("/api")
@RestController
public class RestAPIController {
	
	private final ExpensesService expensesService; 
	
	@Autowired
	public RestAPIController(ExpensesService expensesService) {
		this.expensesService = expensesService;
	}

	@GetMapping("/getExpenses")
	public String getExpenses(@RequestParam(value = "id") int id) {
		//checking for correct id here?
		List<Expenses> expenses = expensesService.getExpensesByOwnerID(id);
		return expenses.toString(); 
	}

}
