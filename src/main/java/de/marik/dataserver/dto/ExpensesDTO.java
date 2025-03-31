package de.marik.dataserver.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public class ExpensesDTO {
	
	private int id;

	@NotNull(message = "amount should be provided")
	@DecimalMin(value = "0.0", message = "amount should be a positive number")
	private Double amount;

	@NotNull(message = "date should be provided")
	@PastOrPresent(message = "date cannot be in the future")
	private LocalDate date;

	@NotEmpty(message = "some information about expenses should be provided")
	@Size(max = 250, message = "notice should not be longer than 250 symbols")
	private String comment;

	private int ownerIdentity;
	
	public ExpensesDTO() {
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getOwnerIdentity() {
		return ownerIdentity;
	}

	public void setOwnerIdentity(int ownerIdentity) {
		this.ownerIdentity = ownerIdentity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ExpensesDTO [amount=" + amount + ", date=" + date + ", comment=" + comment + ", ownerIdentity="
				+ ownerIdentity + "]";
	}
}
