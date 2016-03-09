package com.viglucci.app.handler;

import com.viglucci.app.jade.Jade4J;
import org.cef.callback.CefCallback;
import org.cef.network.CefRequest;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Kevin
 * @since 12/20/2015.
 */
public class TemplateSchemaHandler extends SchemaHandler  {

	public static final String scheme = "template";
	public static final String domain = "";

	public TemplateSchemaHandler() {
		super();
	}

	@Override
	public synchronized boolean processRequest(CefRequest request, CefCallback callback) {
		boolean handled = false;
		String url = request.getURL();
		String loadPath = url.substring(url.indexOf('/') + 2);
		if (url.endsWith(".jade")) {
			try {
				byte[] bytes = Jade4J.renderAsByteArray(loadPath, new HashMap<>());
				setData_(bytes);
				handled = true;
				setMime_type_("text/html");
			} catch(Exception e){
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}

		if (handled) {
			// Indicate the headers are available.
			callback.Continue();
			return true;
		}

		System.out.println("TemplateSchemaHandler Failed to respond to local resource: " + loadPath);

		return false;
	}

	@Override
	protected boolean loadContent(String loadPath) {
		try {
			setData_(Jade4J.renderAsByteArray("index.jade", new HashMap<>()));
			return true;
		} catch(IOException e){}
		return false;
	}


}
