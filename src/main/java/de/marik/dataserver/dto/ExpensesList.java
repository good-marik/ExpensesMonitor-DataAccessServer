package de.marik.dataserver.dto;

import java.util.List;

public class ExpensesList {
	private List<ExpensesDTO> expensesList;

	public ExpensesList(List<ExpensesDTO> expensesList) {
		this.expensesList = expensesList;
	}

	public List<ExpensesDTO> getExpensesList() {
		return expensesList;
	}

	public void setExpensesList(List<ExpensesDTO> expensesList) {
		this.expensesList = expensesList;
	}
}
