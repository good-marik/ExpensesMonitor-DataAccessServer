package de.marik.apigateway.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.marik.apigateway.models.Expenses;
import de.marik.apigateway.repositories.ExpensesRepository;

@Service
public class ExpensesService {

	private final ExpensesRepository expensesRepository;

	@Autowired
	public ExpensesService(ExpensesRepository expensesRepository) {
		this.expensesRepository = expensesRepository;
	}

	public List<Expenses> getAllExpenses() {
		return expensesRepository.findAll();
	}

	@Transactional
	public void add(Expenses expenses) {
		expensesRepository.save(expenses);
		
	}

}
