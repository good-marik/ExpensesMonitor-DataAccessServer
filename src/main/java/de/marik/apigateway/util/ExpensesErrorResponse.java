package de.marik.apigateway.util;

import java.time.LocalDate;

public class ExpensesErrorResponse {
	private String message;
	private String timestamp;

	public ExpensesErrorResponse(String message) {
		this.message = message;
		this.timestamp = LocalDate.now().toString();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
