package org.qsexam.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Qsexam_webapp implements EntryPoint {
	
	private static final String JSON_URL = "http://localhost:8080/qsexam-rest/store/json?name=";
	private Store store;
	private HorizontalPanel findPanel = new HorizontalPanel();
	private TextBox storeNameTextBox = new TextBox();
	private Button findStoreButton = new Button("Find");
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label nameLabel = new Label();
	private Label contactLabel = new Label();
	private FlexTable salesFlexTable = new FlexTable();
	private Label errorMsgLabel = new Label();
	
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
		errorMsgLabel.setVisible(false);
		mainPanel.add(errorMsgLabel);
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
		String url = JSON_URL + storeNameTextBox.getText().trim();
		System.out.println(url);
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
		
		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					displayError("Couldn't retrieve JSON");
				}
				
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						updateStore(asStore(response.getText()));
					} else {
						displayError("Couldn't retrieve JSON (" + response.getStatusCode() + ")");
					}
				}
			});
		} catch (RequestException e) {
			displayError("Couldn't retrieve JSON");
		}
	}
	
	private void updateStore(Store store) {
		System.out.println("Showing: " + store.getName());
		nameLabel.setText(store.getName());
		//Clear errors
		errorMsgLabel.setVisible(false);
	}

	/**
	 * If can't get JSON, display error message.
	 * 
	 * @param string
	 */
	private void displayError(String error) {
		errorMsgLabel.setText("Error: " + error);
		errorMsgLabel.setVisible(true);
	}

	private final native Store asStore(String json) /*-{
		return eval(json);
	}-*/;
}
