package de.marik.dataserver.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.marik.dataserver.dto.ExpensesDTO;
import de.marik.dataserver.dto.ExpensesList;
import de.marik.dataserver.models.Expenses;
import de.marik.dataserver.models.Person;
import de.marik.dataserver.repositories.ExpensesRepository;
import de.marik.dataserver.repositories.PersonRepository;
import de.marik.dataserver.utils.ExpensesException;

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

	//TODO: refractoring
	public ExpensesList getExpensesByOwnerID(int id) {
		Optional<Person> person = personRepository.findById(id);
		if (person.isEmpty())
			throw new ExpensesException("User with id=" + id + " is not found.");

		int ownerIdentity = person.get().getId();
		List<ExpensesDTO> expensesDTO = new ArrayList<ExpensesDTO>();
		for (Expenses e : person.get().getExpenses()) {
			ExpensesDTO eDTO = convertToExpensesDTOAndEnrich(e);
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
			throw new ExpensesException("Expenses with id=" + id + " is not found.");
	}

	@Transactional
	public ExpensesDTO createExpenses(ExpensesDTO expensesDTO) {
		Expenses expenses = expensesRepository.save(convertToExpensesAndEnrich(expensesDTO)); 
		return convertToExpensesDTOAndEnrich(expenses);
	}

	public ExpensesDTO getExpensesById(int id) {
		Optional<Expenses> expenses = expensesRepository.findById(id);
		if (expenses.isEmpty())
			throw new ExpensesException("Expenses with id=" + id + " is not found.");
		ExpensesDTO expensesDTO = convertToExpensesDTOAndEnrich(expenses.get());
		return expensesDTO;
	}

	@Transactional
	public ExpensesDTO update(ExpensesDTO expensesDTO) {
		Expenses expenses = convertToExpensesAndEnrich(expensesDTO);
		Optional<Expenses> expensesOptional = expensesRepository.findById(expenses.getId());
		if (expensesOptional.isEmpty())
			throw new ExpensesException("Expenses with id=" + expenses.getId() + " is not found.");
		Expenses expensesToUpdate = expensesOptional.get();
		if (expensesToUpdate.getOwner() != expenses.getOwner())
			throw new ExpensesException("The owner id for this expenses is incorrect.");

		expensesToUpdate.setAmount(expenses.getAmount());
		expensesToUpdate.setDate(expenses.getDate());
		expensesToUpdate.setComment(expenses.getComment());
		return convertToExpensesDTOAndEnrich(expensesRepository.save(expensesToUpdate));
	}

	private Expenses convertToExpensesAndEnrich(ExpensesDTO expensesDTO) {
		Expenses expenses = modelMapper.map(expensesDTO, Expenses.class);
		Person person = personRepository.findById(expensesDTO.getOwnerIdentity()).get();
		expenses.setOwner(person);
		expenses.setAmount(roundToTwoDecimals(expenses.getAmount()));
		return expenses;
	}

	private ExpensesDTO convertToExpensesDTOAndEnrich(Expenses expenses) {
		ExpensesDTO expensesDTO = modelMapper.map(expenses, ExpensesDTO.class);
		expensesDTO.setOwnerIdentity(expenses.getOwner().getId());
		return expensesDTO;
	}

	private double roundToTwoDecimals(double amount) {
		return new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

}
