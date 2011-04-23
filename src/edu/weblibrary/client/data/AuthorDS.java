package edu.weblibrary.client.data;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.OperationBinding;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;

public class AuthorDS extends RestDataSource {
	
	private static AuthorDS instance = null;

	public static AuthorDS getInstance() {
		if (instance == null) {
			instance = new AuthorDS("authorDS");
		}
		return instance;
	}
	
	private AuthorDS(String id) {
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
		
		DataSourceIntegerField authorId = new DataSourceIntegerField("id");
		DataSourceTextField authorName = new DataSourceTextField("name");
		DataSourceTextField authorSurname = new DataSourceTextField("surname");
		DataSourceTextField authorPatronymic = new DataSourceTextField("patronymic");
		DataSourceIntegerField authorBirthYear = new DataSourceIntegerField("birthYear");
		
		authorName.setRequired(true);
		authorSurname.setRequired(true);
		authorBirthYear.setRequired(true);
		
		IntegerRangeValidator authorBirthYearValidator = new IntegerRangeValidator();
		authorBirthYearValidator.setMin(0);
		authorBirthYearValidator.setMax(9999);
		authorBirthYear.setValidators(authorBirthYearValidator);
		
		authorId.setPrimaryKey(true);

		setFields(authorId, authorName, authorSurname, authorPatronymic, authorBirthYear);
		
		setDataURL("rest_author");
	}	
}
