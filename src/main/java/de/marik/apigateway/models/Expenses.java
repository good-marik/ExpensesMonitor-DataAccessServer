package de.marik.apigateway.models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Expenses")
public class Expenses {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@DecimalMin(value = "0.0", message = "positive numbers only!") //is constrain working?
	private double amount; // or replace with Double?

	@Temporal(TemporalType.DATE)
	private LocalDate date;

	@Size(max = 250, message = "Too long comment!")
	private String comment;

	@ManyToOne
	@JoinColumn(name = "owner", referencedColumnName = "id")
	private Person owner;
	
	
	
//	public Expenses(double amount, String comment) {
//		this.amount = amount;
//		this.comment = comment;
//	}

	public Expenses() {
	}


	public Expenses(double amount, String comment, Person owner) {
		this.amount = amount;
		this.comment = comment;
		this.owner = owner;
		this.date = LocalDate.now();
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
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

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "Expenses [id=" + id + ", amount=" + amount + ", date=" + date + ", comment=" + comment + "]";
	}

}
