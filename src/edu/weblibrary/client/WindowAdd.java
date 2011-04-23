package edu.weblibrary.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.util.KeyCallback;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.HiddenValidationErrorsEvent;
import com.smartgwt.client.widgets.form.events.HiddenValidationErrorsHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class WindowAdd extends Window {
	DynamicForm form;
	IButton okButton = new IButton("Добавить");
	IButton cancelButton = new IButton("Отмена");
	HLayout buttonsLayout = new HLayout(5);
	VLayout mainLayout = new VLayout();
	
	public WindowAdd(final DynamicForm form) {		
		if (!GWT.isScript()) {
		    KeyIdentifier debugKey = new KeyIdentifier();
		    debugKey.setCtrlKey(true);
		    debugKey.setKeyName("[");

		    Page.registerKey(debugKey, new KeyCallback() {
		        public void execute(String keyName) {
		            SC.showConsole();
		        }
		    });
		}		
		this.form = form;
		
		okButton.setIcon("[SKIN]/actions/ok.png");
		//cancelButton.setIcon("[SKIN]/actions/cancel.png");
		
		setTitle("Добавить запись");		
		setHeaderControls(HeaderControls.HEADER_LABEL, HeaderControls.CLOSE_BUTTON);
		setIsModal(true);
		setShowModalMask(true);		
		setAutoCenter(true);
		
		mainLayout.addMember(form);
		mainLayout.addMember(buttonsLayout);
		mainLayout.setMembersMargin(10);
		mainLayout.setLayoutMargin(10);
				
		buttonsLayout.addMember(okButton);
		buttonsLayout.addMember(cancelButton);
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setMargin(10);
		
		addItem(mainLayout);
		addItem(buttonsLayout);
		
		form.addHiddenValidationErrorsHandler(new HiddenValidationErrorsHandler() {
			public void onHiddenValidationErrors(HiddenValidationErrorsEvent event) {
				
				String errors = new String();					
				for (Object v: event.getErrors().values())
					errors += (String) v + "\r\n";
					
				SC.say("Не удалось добавить запись", errors);				
				event.cancel();
			}
		});		
						
		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form.validate()) {					
					DSRequest req = new DSRequest();
					req.setWillHandleError(true);
					
					form.saveData(new DSCallback() {
						public void execute(DSResponse response,
								Object rawData, DSRequest request) {
							GWT.log("Status: " + String.valueOf(response.getStatus()));
							
							if(response.getStatus() == RPCResponse.STATUS_SUCCESS) {
								hide();
							} else {
								SC.say("Ошибка", "Не удалось добавить запись");
							}
						}						
					}, req);
				}				
			}
		});
		
		cancelButton.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {				
				hide();
			}
		});
	}	

	public void addNewRecord() {
		form.clearValues();
		show();		
	}
}
