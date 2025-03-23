package de.marik.apigateway.util;

public class ExpensesNotCreatedException extends RuntimeException {
	private static final long serialVersionUID = -605184224235626416L;

	public ExpensesNotCreatedException(String message) {
		super(message);
	}
}
