package de.marik.dataserver.controllers;

import java.time.format.DateTimeParseException;
import java.util.List;

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
	public ResponseEntity<ExpensesList> getExpenses(@RequestParam int ownerId) {
		ExpensesList expensesList = expensesService.getExpensesByOwnerID(ownerId);
		return ResponseEntity.ok(expensesList);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteExpensesNew(@RequestParam int id) {
		expensesService.deleteExpenses(id);
		return ResponseEntity.ok("Expenses has been deleted succesfully.");
	}

	@PostMapping("/create")
	public ResponseEntity<ExpensesDTO> addExpensesNew(@RequestBody @Valid ExpensesDTO expensesDTO,
			BindingResult bindingResult) {
		expensesDTO.setId(0); // ignoring id-field in DTO for new entities, if provided in JSON
		expensesDTOValidator.validate(expensesDTO, bindingResult);
		if (bindingResult.hasErrors())
			throw new ExpensesException(buildErrorMessage(bindingResult));
		return ResponseEntity.status(HttpStatus.CREATED).body(expensesService.createExpenses(expensesDTO));
	}

	@GetMapping("/expensesById")
	public ResponseEntity<ExpensesDTO> getExpensesByIdNew(@RequestParam int id) {
		return ResponseEntity.ok(expensesService.getExpensesById(id));
	}

	// TODO: change to PatchMapping
	@PostMapping("/update")
	public ResponseEntity<ExpensesDTO> updateExpenses(@RequestBody @Valid ExpensesDTO expensesDTO,
			BindingResult bindingResult) {
		expensesDTOValidator.validate(expensesDTO, bindingResult);
		if (bindingResult.hasErrors())
			throw new ExpensesException(buildErrorMessage(bindingResult));
		return ResponseEntity.ok(expensesService.update(expensesDTO));
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
