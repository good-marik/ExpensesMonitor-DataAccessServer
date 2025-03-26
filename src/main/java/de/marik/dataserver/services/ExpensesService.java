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
import de.marik.dataserver.models.Expenses;
import de.marik.dataserver.models.Person;
import de.marik.dataserver.repositories.ExpensesRepository;
import de.marik.dataserver.repositories.PersonRepository;

@Service
@Transactional(readOnly = true)
public class ExpensesService {
	private final ExpensesRepository expensesRepository;
	private final PersonRepository personRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public ExpensesService(ExpensesRepository expensesRepository, PersonRepository personRepository,
			ModelMapper modelMapper) {
		this.expensesRepository = expensesRepository;
		this.personRepository = personRepository;
		this.modelMapper = modelMapper;
	}

//	public List<Expenses> getAllExpenses() {
//		return expensesRepository.findAll();
//	}

	@Transactional
	public void saveExpenses(ExpensesDTO expensesDTO) {
		Expenses expenses = convertToExpenses(expensesDTO);
		Person person = personRepository.findById(expensesDTO.getOwnerIdentity()).get();
		expenses.setOwner(person);
		expenses.setAmount(roundToTwoDecimals(expenses.getAmount()));
		expensesRepository.save(expenses);
	}

	public List<ExpensesDTO> getExpensesByOwnerID(int id) {
		Optional<Person> person = personRepository.findById(id);
		if (person.isEmpty())
			return Collections.emptyList();

		int ownerIdentity = person.get().getId();
		List<ExpensesDTO> expensesDTO = new ArrayList<ExpensesDTO>();
		for (Expenses e : person.get().getExpenses()) {
			ExpensesDTO eDTO = convertToExpensesDTO(e);
			eDTO.setOwnerIdentity(ownerIdentity);
			expensesDTO.add(eDTO);
		}
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
