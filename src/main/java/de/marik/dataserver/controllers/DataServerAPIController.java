package de.marik.dataserver.controllers;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.marik.dataserver.dto.ExpensesDTO;
import de.marik.dataserver.dto.ExpensesList;
import de.marik.dataserver.models.Expenses;
import de.marik.dataserver.services.ExpensesService;
import de.marik.dataserver.utils.ExpensesDTOValidator;
import de.marik.dataserver.utils.ExpensesErrorResponse;
import de.marik.dataserver.utils.ExpensesException;
import jakarta.validation.Valid;

@RequestMapping("/api")
@RestController
public class DataServerAPIController {

	private final ExpensesService expensesService;
	private final ExpensesDTOValidator expensesDTOValidator;

	public DataServerAPIController(ExpensesService expensesService, ExpensesDTOValidator expensesDTOValidator) {
		this.expensesService = expensesService;
		this.expensesDTOValidator = expensesDTOValidator;
	}

	@GetMapping("/expenses")
	public ResponseEntity<Object> getExpenses(@RequestParam int ownerId) {
		if (ownerId <= 0)
			return ResponseEntity.badRequest().body(Map.of("error", "Invalid ownerId "));
		ExpensesList expensesList = expensesService.getExpensesByOwnerID(ownerId);
		return ResponseEntity.ok(expensesList);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteExpensesNew(@RequestParam int id) {
		try {
			expensesService.deleteExpenses(id);
			return ResponseEntity.ok("Expenses has been deleted succesfully.");
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body("Invalid expenses id ");
		}
	}
	
	@PostMapping("/create")
	public ResponseEntity<Expenses> addExpensesNew(@RequestBody @Valid ExpensesDTO expensesDTO, BindingResult bindingResult) {
		expensesDTO.setId(0); // ignoring id-field in DTO for new entities, if provided in JSON
		expensesDTOValidator.validate(expensesDTO, bindingResult);
		
		if (bindingResult.hasErrors()) {
			throw new ExpensesException(buildErrorMessage(bindingResult));
		}
		Expenses expenses = expensesService.createExpenses(expensesDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(expenses);
	}
	
	
	
	
	
	@PostMapping("/addExpenses")
	public ResponseEntity<HttpStatus> addExpenses(@RequestBody @Valid ExpensesDTO expensesDTO,
			BindingResult bindingResult) {
		// TODO: work on return type!

		expensesDTOValidator.validate(expensesDTO, bindingResult);
		if (bindingResult.hasErrors()) {
			String errorMessage = buildErrorMessage(bindingResult);
			throw new ExpensesException(errorMessage);
		}
		expensesService.saveExpenses(expensesDTO);
		return ResponseEntity.ok(HttpStatus.OK); // status 200
	}
	
	

	@PostMapping("/updateExpenses")
	public void updateExpenses(@RequestBody @Valid ExpensesDTO expensesDTO, BindingResult bindingResult) {

		// dublicating addExpenses() - not good
		expensesDTOValidator.validate(expensesDTO, bindingResult);
		if (bindingResult.hasErrors()) {
			String errorMessage = buildErrorMessage(bindingResult);
			throw new ExpensesException(errorMessage);
		}
		expensesService.update(expensesDTO);
	}

	@GetMapping("/getExpensesById")
	public ExpensesDTO getExpensesById(@RequestParam int id) {
		return expensesService.getExpensesById(id);
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
	private ResponseEntity<ExpensesErrorResponse> handleException(ExpensesException e) {
		ExpensesErrorResponse response = new ExpensesErrorResponse(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	private ResponseEntity<ExpensesErrorResponse> handleException(DateTimeParseException e) {
		ExpensesErrorResponse response = new ExpensesErrorResponse(e.getMessage() + "; required format: YYYY-MM-DD");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	private ResponseEntity<ExpensesErrorResponse> handleException(HttpMessageNotReadableException e) {
		ExpensesErrorResponse response = new ExpensesErrorResponse(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	

}
