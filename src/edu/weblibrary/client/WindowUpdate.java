package edu.weblibrary.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.HiddenValidationErrorsEvent;
import com.smartgwt.client.widgets.form.events.HiddenValidationErrorsHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class WindowUpdate extends Window {
		DynamicForm form;
		Button okButton = new Button("Сохранить изменения");
		Button cancelButton = new Button("Отмена");
		HLayout buttonsLayout = new HLayout(5);
		VLayout mainLayout = new VLayout();
		
		public WindowUpdate(final DynamicForm form) {
			this.form = form;
			setTitle("Изменение данных");
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
			
			okButton.setAutoFit(true);
			cancelButton.setAutoFit(true);
			
			addItem(mainLayout);
			addItem(buttonsLayout);
			
			form.getValuesManager().addHiddenValidationErrorsHandler(new HiddenValidationErrorsHandler() {
				public void onHiddenValidationErrors(HiddenValidationErrorsEvent event) {
					String errors = new String();
					
					for (Object v: event.getErrors().values())
						errors += (String) v + "\r\n";
						
					SC.say("Не удалось обновить данные", errors);				
					event.cancel();
				}
			});
			
			okButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (form.validate()) {					
						DSRequest req = new DSRequest();
						req.setWillHandleError(true);					

						form.getValuesManager().saveData(new DSCallback() {
							public void execute(DSResponse response,
									Object rawData, DSRequest request) {
								GWT.log("Status: " + String.valueOf(response.getStatus()));
								
								if(response.getStatus() == response.STATUS_SUCCESS) {
									hide();
									form.clearValues();
								} else {
									SC.say("Ошибка", "Не удалось обновить запись");
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
		
		public void editSelectedData(ListGrid listGrid) {
			form.getValuesManager().clearValues();
			form.getValuesManager().editSelectedData(listGrid);
			form.rememberValues();
		}
	}
