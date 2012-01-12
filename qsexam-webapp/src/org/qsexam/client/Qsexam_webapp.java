package org.qsexam.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Qsexam_webapp implements EntryPoint {
	
	private static final String JSON_URL = ""
	private Store store;
	private HorizontalPanel findPanel = new HorizontalPanel();
	private TextBox storeNameTextBox = new TextBox();
	private Button findStoreButton = new Button("Find");
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label nameLabel = new Label();
	private Label contactLabel = new Label();
	private FlexTable salesFlexTable = new FlexTable();
	
	public void onModuleLoad() {
		//Assemble Find Store panel
		findPanel.add(storeNameTextBox);
		findPanel.add(findStoreButton);
		
		//Create table for sales data
		salesFlexTable.setText(0, 0, "Year");
		salesFlexTable.setText(0, 1, "Week");
		salesFlexTable.setText(0, 2, "Sales $");
		salesFlexTable.setText(0, 3, "Sales U");
		salesFlexTable.setText(0, 4, "In Transit");
		salesFlexTable.setText(0, 5, "On Hand");
		
		//Assemble Main panel
		mainPanel.add(findPanel);
		mainPanel.add(nameLabel);
		mainPanel.add(contactLabel);
		mainPanel.add(salesFlexTable);
		
		//Associate the Main panel with the HTML host page
		RootPanel.get("storeLookup").add(mainPanel);
		
		//Move cursor focus to input box
		storeNameTextBox.setFocus(true);
		
		//Listen for mouse events on the Find button
		findStoreButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				findStore();
			}
		});
		
		//Listen for keyboard events in the input box
		storeNameTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					findStore();
				}
			}
		});
	}

	private void findStore() {
		// TODO Auto-generated method stub
		
	}
	
	private final native JsArray<Store> asArrayOfStore(String json) /* - {
		return eval(json);
	}-*/;
}
