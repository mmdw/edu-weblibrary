package edu.weblibrary.client;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

import edu.weblibrary.client.data.AuthorDS;

public class AuthorGrid extends ListGrid {	
	public AuthorGrid() {
		setDataSource(AuthorDS.getInstance());
		
		ListGridField authorIdField = new ListGridField("id");
		authorIdField.setHidden(true);
		
		setFields(
			authorIdField,				
			new ListGridField("surname", "Фамилия"),
			new ListGridField("name", "Имя"), 
			new ListGridField("patronymic", "Отчество"),
			new ListGridField("birthYear", "Год рождения")
		);		
		
		setEmptyCellValue("--");
		setDataPageSize(80);		
		setAutoFetchData(true);
		setCanEdit(false);
		setEditByCell(false);	
	}
}