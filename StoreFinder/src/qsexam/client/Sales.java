package qsexam.client;

import com.google.gwt.core.client.JavaScriptObject;

public class Sales extends JavaScriptObject{
	
	protected Sales() {}

	public final native int getYear() /*-{ return this.year; }-*/;
	public final native int getWeek() /*-{ return this.week; }-*/;
	public final native double getSales() /*-{ return this.sales; }-*/;
	public final native int getSalesU() /*-{ return this.salesu; }-*/;
	public final native int getInTransit() /*-{ return this.intransit; }-*/;
	public final native int getStoreOH() /*-{ return this.storeoh; }-*/;
}
