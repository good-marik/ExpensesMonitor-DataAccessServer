package de.marik.dataserver.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class DataServerRestAPIController {

	@Value("${spring.datasource.url}")
	private String testVariable;

	private final ExpensesService expensesService;
	private final ExpensesDTOValidator expensesDTOValidator;

	public DataServerRestAPIController(ExpensesService expensesService, ExpensesDTOValidator expensesDTOValidator) {
		this.expensesService = expensesService;
		this.expensesDTOValidator = expensesDTOValidator;
	}

	@GetMapping("/expenses")
	public ResponseEntity<Object> getExpenses(@RequestParam int ownerId) {
		if(ownerId <= 0) return ResponseEntity.badRequest().body(Map.of("error", "Invalid ownerId"));
		ExpensesList expensesList = expensesService.getExpensesByOwnerID(ownerId);
		return ResponseEntity.ok(expensesList);
	}
	
//	@GetMapping("/getExpenses")
//	public ExpensesList getExpensesOLD(@RequestParam int id) {
//		return new ExpensesList(expensesService.getExpensesByOwnerID(id).stream().collect(Collectors.toList()));
//	}
	
	
	
	@PostMapping("/updateExpenses")
	public void updateExpenses(@RequestBody @Valid ExpensesDTO expensesDTO, BindingResult bindingResult) {
		
		//dublicating addExpenses() - not good
		expensesDTOValidator.validate(expensesDTO, bindingResult);
		if(bindingResult.hasErrors()) {
			String errorMessage = buildErrorMessage(bindingResult);
			throw new ExpensesException(errorMessage);
		}
		expensesService.update(expensesDTO);
	}
	

	@DeleteMapping("/deleteExpenses")
	//return a proper JSON later?
	public void deleteExpenses(@RequestParam int id) {
		expensesService.delete(id);
	}
	
	
	@GetMapping("/getExpensesById")
	public ExpensesDTO getExpensesById(@RequestParam int id) {
		return expensesService.getExpensesById(id);
	}

	@PostMapping("/addExpenses")
	public ResponseEntity<HttpStatus> addExpenses(@RequestBody @Valid ExpensesDTO expensesDTO,
			BindingResult bindingResult) {
		//TODO: work on return type!
		
		expensesDTOValidator.validate(expensesDTO, bindingResult);
		if (bindingResult.hasErrors()) {
			String errorMessage = buildErrorMessage(bindingResult);
			throw new ExpensesException(errorMessage);
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
	private ResponseEntity<ExpensesErrorResponse> handleException(ExpensesException e) {
		ExpensesErrorResponse response = new ExpensesErrorResponse(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 4xx error
	}

}
