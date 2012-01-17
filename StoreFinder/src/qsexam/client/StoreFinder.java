package qsexam.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StoreFinder implements EntryPoint {

	private static final String JSON_URL = "http://qsexam.the-treasury.net/store/show?q=";
	
	//Widgets
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label errorMsgLabel = new Label();
	private HorizontalPanel findPanel = new HorizontalPanel();
	private TextBox findTextBox = new TextBox();
	private Button findButton = new Button("Find");
	private Label nameLabel = new Label();
	private Label contactLabel = new Label();
	private Grid salesGrid = new Grid(1, 6);
	
	//Data
	private int jsonRequestId = 0;
	private List<Sales> salesList = new ArrayList<Sales>();
	
	public void onModuleLoad() {
		//Assemble Find Store Panel
		findPanel.add(findTextBox);
		findPanel.add(findButton);
		findPanel.addStyleName("center");
		
		//Create the sales FlexTable
		salesGrid.setText(0, 0, "Year");
		salesGrid.setText(0, 1, "Week");
		salesGrid.setText(0, 2, "Sales $");
		salesGrid.setText(0, 3, "Sales U");
		salesGrid.setText(0, 4, "In Transit");
		salesGrid.setText(0, 5, "On Hand");

		salesGrid.getRowFormatter().addStyleName(0, "salesListHeader");
		salesGrid.addStyleName("salesList center");
		
		//Assemble main panel
		errorMsgLabel.setVisible(false);
		salesGrid.setVisible(false);
		
		nameLabel.addStyleName("nameLabel center");
		contactLabel.addStyleName("contactLabel center");
		
		mainPanel.add(errorMsgLabel);
		mainPanel.add(findPanel);
		mainPanel.add(nameLabel);
		mainPanel.add(contactLabel);
		mainPanel.add(salesGrid);
		
		mainPanel.addStyleName("center");
		
		//Associate main panel to host page
		RootPanel.get("storeFinder").add(mainPanel);
		
		//Move cursor focus to input box
		findTextBox.setFocus(true);
		
		//Listen for mouse events on the Find button
		findButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				findStore();
			}
		});
		
		//Listen for keyboard events in the input box
		findTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					findStore();
				}
			}
		});
	}
	
	

	private void findStore() {
		String url = JSON_URL + findTextBox.getText().trim();
		url = URL.encode(url) + "&callback=";
		getJson(jsonRequestId++, url, this);
	}
	
	private void updatePage(Store store) {
		if (null == store.getName()) {
			nameLabel.setText("No store by that name was found");
			contactLabel.setText("");
			salesGrid.setVisible(false);
			return;
		}
		nameLabel.setText(store.getName());
		contactLabel.setText(store.getAddress() + ", " + store.getContactEmail());
		salesGrid.setVisible(false);
		updateTable(store.getSales());
	}
	
	private void updateTable(JsArray<Sales> sales) {
		salesGrid.resizeRows(sales.length()+1);
		for(int i = 0; i < sales.length(); i++){
			NumberFormat currencyFormat = NumberFormat.getCurrencyFormat();
			salesGrid.setText(i+1, 0, Integer.toString(sales.get(i).getYear()));
			salesGrid.setText(i+1, 1, Integer.toString(sales.get(i).getWeek()));
			salesGrid.setText(i+1, 2, currencyFormat.format(sales.get(i).getSales()));
			salesGrid.setText(i+1, 3, Integer.toString(sales.get(i).getSalesU()));
			salesGrid.setText(i+1, 4, Integer.toString(sales.get(i).getInTransit()));
			salesGrid.setText(i+1, 5, Integer.toString(sales.get(i).getStoreOH()));
			
			salesGrid.getCellFormatter().addStyleName(i+1, 0, "salesListColumn salesListDateColumn");
			salesGrid.getCellFormatter().addStyleName(i+1, 1, "salesListColumn salesListDateColumn");
			salesGrid.getCellFormatter().addStyleName(i+1, 2, "salesListColumn salesListNumericColumn");
			salesGrid.getCellFormatter().addStyleName(i+1, 3, "salesListColumn salesListNumericColumn");
			salesGrid.getCellFormatter().addStyleName(i+1, 4, "salesListColumn salesListNumericColumn");
			salesGrid.getCellFormatter().addStyleName(i+1, 5, "salesListColumn salesListNumericColumn");
		}
		salesGrid.setVisible(true);
	}
	
	private void displayError(String error) {
		errorMsgLabel.setText("Error: " + error);
		errorMsgLabel.setVisible(true);
	}
	
	public void handleJsonResponse(JavaScriptObject jso) {
		if (jso == null) {
			displayError("Couldn't retrieve JSON");
			return;
		}
		
		updatePage(asStore(jso));
		errorMsgLabel.setVisible(false);
	}
	
	public native static void getJson(int requestId, String url, StoreFinder handler) /*-{
		var callback = "callback" + requestId;
		
		var script = document.createElement("script");
		script.setAttribute("src", url+callback);
		script.setAttribute("type", "text/javascript");
		
		window[callback] = function(jsonObj) {
handler.@qsexam.client.StoreFinder::handleJsonResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(jsonObj);
			window[callback + "done"] = true;
		}
		
		setTimeout(function() {
			if (!window[callback + "done"]) {
handler.@qsexam.client.StoreFinder::handleJsonResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(null);
			}
			
			document.body.removeChild(script);
			delete window[callback];
			delete window[callback + "done"];
		}, 1000);
		
		document.body.appendChild(script);
	}-*/;
	
	private final native Store asStore(JavaScriptObject jso) /*-{
		return jso;
	}-*/;
	

}
