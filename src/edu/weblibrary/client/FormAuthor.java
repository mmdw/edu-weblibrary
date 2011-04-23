package edu.weblibrary.client;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

import edu.weblibrary.client.data.AuthorDS;

public class FormAuthor extends DynamicForm {	
	public FormAuthor() {
		setDataSource(AuthorDS.getInstance());
		
		IntegerItem id = new IntegerItem("id");
		TextItem surnaname = new TextItem("surname", "Фамилия");
		TextItem name = new TextItem("name", "Имя");		
		TextItem patronymic = new TextItem("patronymic", "Отчество");
		IntegerItem birthYear = new IntegerItem("birthYear", "Год рождения");
		
		setFields(id, name, surnaname, patronymic, birthYear);	
		setBrowserSpellCheck(false);
		
		id.setVisible(false);
		
		final ValuesManager vm = new ValuesManager();
		vm.setDataSource(AuthorDS.getInstance());
		setValuesManager(vm);
	}
}
