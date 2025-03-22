package de.marik.apigateway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.marik.apigateway.models.Expenses;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Integer>{
	
}
