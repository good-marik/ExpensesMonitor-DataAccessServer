package de.marik.apigateway.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Person")
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Size(min = 1, max = 100, message = "Username should be between 1 and 100 symbols long")
	private String username;

	@Size(min = 4, max = 100, message = "Password should be at least 4 symbols long")
	private String password;
	
	@Transient
	private String passwordRepeat;
	
	@OneToMany(mappedBy = "owner")
	//@JsonManagedReference	// to prevent looping
	@JsonBackReference	// to prevent looping
	private List<Expenses> expenses;

	public Person() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordRepeat() {
		return passwordRepeat;
	}

	public void setPasswordRepeat(String passwordRepeat) {
		this.passwordRepeat = passwordRepeat;
	}

	public List<Expenses> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Expenses> expenses) {
		this.expenses = expenses;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", username=" + username + "]";
	}
}
