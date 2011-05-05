package edu.weblibrary.client.data;

import com.smartgwt.client.data.OperationBinding;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.DSProtocol;

public class BookDS extends RestDataSource {
	private static BookDS instance = null;
	
	public static BookDS getInstance() {
		if (instance == null) {
			instance = new BookDS("bookDS");
		}
		
		return instance;
	}
	
	private BookDS(String id) {
		setID(id);
		
		OperationBinding fetch = new OperationBinding();
		fetch.setOperationType(DSOperationType.FETCH);
		fetch.setDataProtocol(DSProtocol.POSTPARAMS);
		
		OperationBinding update = new OperationBinding();
		update.setOperationType(DSOperationType.UPDATE);
		update.setDataProtocol(DSProtocol.POSTPARAMS);
		
		OperationBinding add = new OperationBinding();
		add.setOperationType(DSOperationType.ADD);
		add.setDataProtocol(DSProtocol.POSTPARAMS);
		
		OperationBinding remove = new OperationBinding();
		remove.setOperationType(DSOperationType.REMOVE);
		remove.setDataProtocol(DSProtocol.POSTPARAMS);		
		
		setOperationBindings(fetch, add, update, remove);		
		
		setSendMetaData(true);
		
		DataSourceIntegerField bookId = new DataSourceIntegerField("id");
		DataSourceIntegerField authorId = new DataSourceIntegerField("authorId");
	    DataSourceDateField bookArrivalDate = new DataSourceDateField("arrivalDate");
	    DataSourceTextField bookKeywords = new DataSourceTextField("keywords");
	    DataSourceIntegerField bookQuantity = new DataSourceIntegerField("quantity");
	    DataSourceFloatField bookPrice = new DataSourceFloatField("price");
	    DataSourceIntegerField bookPublicationYear = new DataSourceIntegerField("publicationYear");	    
	    DataSourceTextField bookPublishing = new DataSourceTextField("publishing");
	    DataSourceTextField bookTitle = new DataSourceTextField("title");
	    DataSourceTextField authorInitial = new DataSourceTextField("authorInitial");
	    
	    bookTitle.setRequired(true);
	    bookPublishing.setRequired(true);
	    bookPublicationYear.setRequired(true);
	    bookPrice.setRequired(true);
	    bookQuantity.setRequired(true);
	    bookArrivalDate.setRequired(true);	    
		
		bookId.setPrimaryKey(true);
		authorId.setForeignKey("authorDS.id");		
	
		setFields(authorId, authorInitial, bookId, bookArrivalDate, bookKeywords, 
				bookQuantity, bookPrice, bookPublicationYear, bookPublishing, bookTitle);
		setDataURL("rest_book");		
	}
}
