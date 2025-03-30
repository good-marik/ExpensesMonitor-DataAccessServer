package de.marik.dataserver.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.marik.dataserver.dto.ExpensesDTO;
import de.marik.dataserver.services.PersonService;

@Component
public class ExpensesDTOValidator implements Validator{
	
	private final PersonService personService;
	
	public ExpensesDTOValidator(PersonService personService) {
		this.personService = personService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return ExpensesDTOValidator.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ExpensesDTO expensesDTO = (ExpensesDTO) target;
		if (personService.getPersonById(expensesDTO.getOwnerIdentity()).isEmpty()) {
			errors.rejectValue("ownerIdentity", "", "unknown user");
		}
		
	}

}
