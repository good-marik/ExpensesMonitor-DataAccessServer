package de.marik.apigateway.models;

import java.util.List;

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

//	@NotBlank(message = "Username should not be empty")
	@Size(min = 1, max = 100, message = "Username should be between 1 and 100 symbols long")
	private String username;

	// TODO: validation for a plain password here
	//@NotBlank(message = "Password should not be empty")
	@Size(min = 4, max = 100, message = "Password should be at least 4 symbols long")
	private String password;
	
	@Transient
	private String passwordRepeat;
	
	@OneToMany(mappedBy = "owner")
	private List<Expenses> expenses;
	
	public Person(String username, String password) {
		this.username = username;
		this.password = password;
	}

	// is really needed for Spring?
	public Person() {
	}

	// for testing ONLY. Should be deleted afterwards!
//	public Person(String username) {
//		this.username = username;
//	}

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
