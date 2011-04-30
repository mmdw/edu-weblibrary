package edu.weblibrary.client;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

import edu.weblibrary.client.data.BookDS;

public class BookGrid extends ListGrid {
	BookGrid() {
		setDataSource(BookDS.getInstance());	
		
		setFields(
			new ListGridField("title", "Название"),
			new ListGridField("authorInitial","Автор"),
			new ListGridField("publishing", "Издательство"),
			new ListGridField("quantity", "Количество"),
			new ListGridField("arrivalDate", "Дата поступления"),
			new ListGridField("price", "цена"),
			new ListGridField("publicationYear", "Год издания"),
			new ListGridField("keywords", "Ключевые слова")
		);
		
		setAutoFetchData(true);
		setCanEdit(false);
		setEditByCell(false);
		setEmptyCellValue("--");
		setDataPageSize(80);		
	}
}
