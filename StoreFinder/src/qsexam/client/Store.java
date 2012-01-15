package qsexam.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class Store extends JavaScriptObject{
	protected Store() {}

	public final native String getName() /*-{ return this.name; }-*/;
	public final native String getAddress() /*-{ return this.address; }-*/;
	public final native String getContactEmail() /*-{ return this.contact_email; }-*/;
	public final native JsArray<Sales> getSales() /*-{ return this.sales; }-*/;
}
