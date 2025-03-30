package de.marik.dataserver.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.marik.dataserver.dto.ExpensesDTO;
import de.marik.dataserver.dto.ExpensesList;
import de.marik.dataserver.models.Expenses;
import de.marik.dataserver.models.Person;
import de.marik.dataserver.repositories.ExpensesRepository;
import de.marik.dataserver.repositories.PersonRepository;
import jakarta.validation.Valid;

@Service
@Transactional(readOnly = true)
public class ExpensesService {
	private final ExpensesRepository expensesRepository;
	private final PersonRepository personRepository;
	private final ModelMapper modelMapper;

	public ExpensesService(ExpensesRepository expensesRepository, PersonRepository personRepository,
			ModelMapper modelMapper) {
		this.expensesRepository = expensesRepository;
		this.personRepository = personRepository;
		this.modelMapper = modelMapper;
	}

	public ExpensesList getExpensesByOwnerID(int id) {
		Optional<Person> person = personRepository.findById(id);
		if (person.isEmpty())
			return new ExpensesList(Collections.emptyList());

		int ownerIdentity = person.get().getId();
		List<ExpensesDTO> expensesDTO = new ArrayList<ExpensesDTO>();
		for (Expenses e : person.get().getExpenses()) {
			ExpensesDTO eDTO = convertToExpensesDTO(e);
			eDTO.setOwnerIdentity(ownerIdentity);
			expensesDTO.add(eDTO);
		}
		return new ExpensesList(expensesDTO);
	}

	@Transactional
	public void deleteExpenses(int id) {
		if (expensesRepository.existsById(id))
			expensesRepository.deleteById(id);
		else
			throw new RuntimeException("No Expenses with id=" + " was found");
	}

//	@Transactional
//	public void delete(int id) {
//		expensesRepository.deleteById(id);
//		System.out.println("Successfully deleted: " + id);
//	}

	@Transactional
	public void saveExpenses(ExpensesDTO expensesDTO) {
		Expenses expenses = convertToExpenses(expensesDTO);
		Person person = personRepository.findById(expensesDTO.getOwnerIdentity()).get();
		expenses.setOwner(person);
		expenses.setAmount(roundToTwoDecimals(expenses.getAmount()));
		expensesRepository.save(expenses);
	}

	@Transactional
	public void update(ExpensesDTO expensesDTO) {
		Expenses expenses = convertToExpenses(expensesDTO);
		Optional<Expenses> expensesOptional = expensesRepository.findById(expenses.getId());
		if (expensesOptional.isEmpty())
			return; // TODO: return a proper Response here!
		Expenses expensesToBeUpdated = expensesOptional.get();
		expensesToBeUpdated.setAmount(expenses.getAmount());
		expensesToBeUpdated.setDate(expenses.getDate());
		expensesToBeUpdated.setComment(expenses.getComment());
//		expensesToBeUpdated.setOwner(expenses.getOwner());
		expensesRepository.save(expensesToBeUpdated);
		// TODO: return a proper Response here!
		// return null;
	}

	public ExpensesDTO getExpensesById(int id) {
		Optional<Expenses> expenses = expensesRepository.findById(id);
		if (expenses.isEmpty())
			return null; // TODO: return a proper Response here!
		ExpensesDTO expensesDTO = convertToExpensesDTO(expenses.get());
		expensesDTO.setOwnerIdentity(expenses.get().getOwner().getId()); // TODO: refractoring here, move to the method?
		return expensesDTO;
	}

	private Expenses convertToExpenses(ExpensesDTO expensesDTO) {
		return modelMapper.map(expensesDTO, Expenses.class);
	}

	private ExpensesDTO convertToExpensesDTO(Expenses expenses) {
		return modelMapper.map(expenses, ExpensesDTO.class);
	}

	private double roundToTwoDecimals(double amount) {
		return new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

}
