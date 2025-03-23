package de.marik.apigateway.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.marik.apigateway.models.Expenses;
import de.marik.apigateway.models.Person;
import de.marik.apigateway.services.ExpensesService;
import de.marik.apigateway.services.PersonService;
import de.marik.apigateway.util.ExpensesErrorResponse;
import de.marik.apigateway.util.ExpensesNotCreatedException;
import jakarta.validation.Valid;

@RequestMapping("/api")
@RestController
public class RestAPIController {

	private final ExpensesService expensesService;
	private final PersonService personService;

	@Autowired
	public RestAPIController(ExpensesService expensesService, PersonService personService) {
		this.expensesService = expensesService;
		this.personService = personService;
	}

	@GetMapping("/getExpenses")
	public List<Expenses> getExpenses(@RequestParam(value = "id") int id) {
		// to check for correct id here? not necessary
		return expensesService.getExpensesByOwnerID(id);
	}

	// for testing ONLY
	@GetMapping("/getPeople")
	public List<Person> getPeople() {
		return personService.getAllPeople();
	}

	@PostMapping("/addExpenses")
	public ResponseEntity<HttpStatus> addExpenses(@RequestBody @Valid Expenses expenses, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String errorMessage = buildErrorMessage(bindingResult);
			throw new ExpensesNotCreatedException(errorMessage);
		}
		expensesService.save(expenses);
		return ResponseEntity.ok(HttpStatus.OK); // status 200
	}

	private String buildErrorMessage(BindingResult bindingResult) {
		StringBuilder sb = new StringBuilder();
		List<FieldError> errors = bindingResult.getFieldErrors();
		for (FieldError error : errors) {
			sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
		}
		return sb.toString();
	}
	
	@ExceptionHandler
	private ResponseEntity<ExpensesErrorResponse> handleException(ExpensesNotCreatedException e) {
		ExpensesErrorResponse response = new ExpensesErrorResponse(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);	// 4xx error
	}

}
