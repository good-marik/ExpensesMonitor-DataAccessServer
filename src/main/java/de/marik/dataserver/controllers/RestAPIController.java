package de.marik.dataserver.controllers;

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

import de.marik.dataserver.dto.ExpensesDTO;
import de.marik.dataserver.services.ExpensesService;
import de.marik.dataserver.utils.ExpensesDTOValidator;
import de.marik.dataserver.utils.ExpensesErrorResponse;
import de.marik.dataserver.utils.ExpensesNotCreatedException;
import jakarta.validation.Valid;

@RequestMapping("/api")
@RestController
public class RestAPIController {
	private final ExpensesService expensesService;
	private final ExpensesDTOValidator expensesDTOValidator;

	@Autowired
	public RestAPIController(ExpensesService expensesService, ExpensesDTOValidator expensesDTOValidator) {
		this.expensesService = expensesService;
		this.expensesDTOValidator = expensesDTOValidator;
	}

//  for testing ONLY
//	@GetMapping("/getPeople")
//	public List<Person> getPeople() {
//		return personService.getAllPeople();
//	}

	@GetMapping("/getExpenses")
	public List<ExpensesDTO> getExpenses(@RequestParam(value = "id") int id) {
		return expensesService.getExpensesByOwnerID(id);
	}

	@PostMapping("/addExpenses")
	public ResponseEntity<HttpStatus> addExpenses(@RequestBody @Valid ExpensesDTO expensesDTO,
			BindingResult bindingResult) {
		expensesDTOValidator.validate(expensesDTO, bindingResult);
		if (bindingResult.hasErrors()) {
			String errorMessage = buildErrorMessage(bindingResult);
			throw new ExpensesNotCreatedException(errorMessage);
		}
		expensesService.saveExpenses(expensesDTO);
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
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 4xx error
	}

}
