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
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;

public class StoreFinder implements EntryPoint {

	private static final String JSON_URL = "http://localhost:8080/qsexam/store/show?q=";
	
	//Widgets
	private VerticalPanel mainPanel = new VerticalPanel();
	private Label errorMsgLabel = new Label();
	private HorizontalPanel findPanel = new HorizontalPanel();
	private TextBox findTextBox = new TextBox();
	private Button findButton = new Button("Find");
	private Label nameLabel = new Label();
	private Label contactLabel = new Label();
	private CellTable<Sales> salesCellTable = new CellTable<Sales>();
	
	//Data
	private int jsonRequestId = 0;
	private List<Sales> salesList = new ArrayList<Sales>();
	
	public void onModuleLoad() {
		//Assemble Find Store Panel
		findPanel.add(findTextBox);
		findPanel.add(findButton);
		
		//Create the sales cell table
		initializeSalesTable();
		
		//Assemble main panel
		errorMsgLabel.setVisible(false);
		
		mainPanel.add(errorMsgLabel);
		mainPanel.add(findPanel);
		mainPanel.add(nameLabel);
		mainPanel.add(contactLabel);
		mainPanel.add(salesCellTable);
		
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
	
	private void initializeSalesTable() {
		salesCellTable = new CellTable<Sales>();
		
		//Text Column for Year
		TextColumn<Sales> yearColumn = new TextColumn<Sales>() {
			@Override
			public String getValue(Sales sales) {
				return Integer.toString(sales.getYear());
			}
		};
		salesCellTable.addColumn(yearColumn, "Year");
		
		//Text Column for Week
		TextColumn<Sales> weekColumn = new TextColumn<Sales>() {
			@Override
			public String getValue(Sales sales) {
				return Integer.toString(sales.getWeek());
			}
		};
		salesCellTable.addColumn(weekColumn, "Week");
		
		//Text Column for Sales
		TextColumn<Sales> salesColumn = new TextColumn<Sales>() {
			@Override
			public String getValue(Sales sales) {
				return Double.toString(sales.getSales());
			}
		};
		salesCellTable.addColumn(salesColumn, "Sales $");
		
		//Text Column for Sales U
		TextColumn<Sales> salesUColumn = new TextColumn<Sales>() {
			@Override
			public String getValue(Sales sales) {
				return Integer.toString(sales.getSalesU());
			}
		};
		salesCellTable.addColumn(salesUColumn, "Sales U");
		
		//Text Column for In Transit
		TextColumn<Sales> inTransitColumn = new TextColumn<Sales>() {
			@Override
			public String getValue(Sales sales) {
				return Integer.toString(sales.getInTransit());
			}
		};
		salesCellTable.addColumn(inTransitColumn, "In Transit");
		
		//Text Column for On Hand
		TextColumn<Sales> storeOHColumn = new TextColumn<Sales>() {
			@Override
			public String getValue(Sales sales) {
				return Integer.toString(sales.getStoreOH());
			}
		};
		salesCellTable.addColumn(storeOHColumn, "On Hand");
		
		salesCellTable.setRowData(salesList);
	}

	private void findStore() {
		String url = JSON_URL + findTextBox.getText().trim();
		System.out.println(url);
		url = URL.encode(url) + "&callback=";
		getJson(jsonRequestId++, url, this);
	}
	
	private void updatePage(Store store) {
		System.out.println(store.getName());
		if (null == store.getName()) {
			nameLabel.setText("No store by that name was found");
			contactLabel.setText("");
			updateTable(store.getSales());
			return;
		}
		nameLabel.setText(store.getName());
		contactLabel.setText(store.getAddress() + ", " + store.getContactEmail());
		updateTable(store.getSales());
	}
	
	private void updateTable(JsArray<Sales> sales) {
		//Update sales list
		salesList.clear();
		for(int i = 0; i < sales.length(); i++){
			salesList.add(sales.get(i));
		}
		salesCellTable.setRowCount(sales.length());
		salesCellTable.setVisibleRange(new Range(0, sales.length()));
		salesCellTable.setRowData(0, salesList);
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
