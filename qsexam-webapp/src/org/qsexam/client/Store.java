package org.qsexam.client;

import com.google.gwt.core.client.JavaScriptObject;

public class Store extends JavaScriptObject {
	protected Store() {}
	
	public final native String getName() /*-{return this.name; }-*/;
	public final native String getAddress() /*-{return this.address; }-*/;
	public final native String getContactEmail() /*-{return this.contact_email; }-*/;
}
