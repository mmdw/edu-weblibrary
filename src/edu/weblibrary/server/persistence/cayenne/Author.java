package edu.weblibrary.server.persistence.cayenne;

import java.util.HashMap;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.validation.BeanValidationFailure;
import org.apache.cayenne.validation.ValidationResult;

import edu.weblibrary.server.persistence.cayenne.auto._Author;

public class Author extends _Author {
	public void validateForUpdate(ValidationResult validationResult) {
		super.validateForSave(validationResult);

		if (getName() != null && getName().equals("null")) {
			validationResult.addFailure(new BeanValidationFailure(this, NAME_PROPERTY, "Введите имя"));
		}
		
		if (getName() != null && getName().contains(" ")) {
			validationResult.addFailure(
			 new BeanValidationFailure(this, NAME_PROPERTY, "Пробелы в поле 'имя' не разрешены"));
		}
		
		if (getSurname() != null && getSurname().contains(" ")) {
			validationResult.addFailure(
				new BeanValidationFailure(this, SURNAME_PROPERTY, "Пробелы в поле 'фамилия' не разрешены"));
		}		
	}
	
	protected void validateForSave(ValidationResult validationResult) {	
		validateForUpdate(validationResult);
		
		ObjectContext context = getObjectContext();
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		params.put("name", readProperty("name"));
		params.put("surname", readProperty("surname"));
		params.put("birthYear", readProperty("birthYear"));		
		
		Expression qualifier = 
			Expression.fromString(
					"name = $name and " +
					"surname = $surname and " +
					"birthYear = $birthYear");
		
		qualifier = qualifier.expWithParameters(params);		
		SelectQuery query = new SelectQuery(Author.class);
		query.setQualifier(qualifier);
		
		if (!context.performQuery(query).isEmpty())	{
			validationResult.addFailure(new BeanValidationFailure(
					this,
					"__error",
					"Автор с такими данными уже есть в базе."));		
		}
	}	
}
