package de.marik.dataserver.utils;

import java.time.LocalDateTime;

public class ExpensesErrorResponse {
	private String message;
	private String timestamp;

	public ExpensesErrorResponse(String message) {
		this.message = message;
		this.timestamp = LocalDateTime.now().toString();
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
