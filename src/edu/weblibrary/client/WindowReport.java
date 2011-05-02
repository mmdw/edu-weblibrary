package edu.weblibrary.client;

import com.smartgwt.client.util.Page;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class WindowReport extends Window {
	VLayout  mainLayout = new VLayout();
	HTMLPane htmlPane = new HTMLPane();	
	ToolStrip toolStrip = new ToolStrip();	
	ToolStripButton printButton = new ToolStripButton();
	ToolStripButton pdfButton = new ToolStripButton();
	
	Integer bookId = null;
	
	public WindowReport() {
		setTitle("Отчет");
		
		toolStrip.setWidth100();
		
		htmlPane.setWidth100(); 
		htmlPane.setHeight100();
		htmlPane.setShowEdges(true);  
		htmlPane.setStyleName("exampleTextBlock");  
         
		printButton.setTitle("Печать");
		printButton.setIcon("[SKIN]actions/print.png");
		
		toolStrip.addButton(printButton);
		
		printButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Canvas.printComponents(new Object[]{htmlPane});				
			}
		});
		
		pdfButton.setTitle("Сохранить в PDF");
		pdfButton.setIcon(Page.getAppDir().concat("img/pdf_icon.png"));
		toolStrip.addButton(pdfButton);		

		pdfButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {			
				com.google.gwt.user.client.Window.open(
						"reportBuilder?id=" + bookId + "&type=pdf",
						"_self",
						"");
			}
		});		
		 
		mainLayout.addMember(toolStrip);
		mainLayout.addMember(htmlPane);         
         
         addItem(mainLayout);
	}
	
	public void setBookId(int bookId) {
		this.bookId = new Integer(bookId);

		htmlPane.setContentsURL("reportBuilder?id=" + bookId + "&type=html");
	}
}