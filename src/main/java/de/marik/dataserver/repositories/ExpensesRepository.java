package de.marik.dataserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.marik.dataserver.models.Expenses;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Integer>{
	
}
