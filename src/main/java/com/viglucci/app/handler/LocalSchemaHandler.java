package com.viglucci.app.handler;

import com.viglucci.app.jade.Jade4J;
import org.cef.callback.CefCallback;
import org.cef.handler.CefResourceHandlerAdapter;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @author Kevin
 * @since 12/20/2015.
 */
public class LocalSchemaHandler extends SchemaHandler  {

	public static final String scheme = "local";
	public static final String domain = "";

	public LocalSchemaHandler() {
		super();
	}

	@Override
	public synchronized boolean processRequest(CefRequest request, CefCallback callback) {
		boolean handled = false;
		String url = request.getURL();
		String loadPath = url.substring(url.indexOf('/') + 1);
		if (url.endsWith(".html")) {
			loadPath = "templates" + loadPath;
			handled = loadContent(loadPath);
			setMime_type_("text/html");
			if (!handled) {
				handled = getNotFoundResponse(loadPath);
			}
		} else if (url.endsWith(".css")) {
			loadPath = "css" + loadPath;
			handled = loadContent(loadPath);
			setMime_type_("text/css");
		} else if (url.endsWith(".png")) {
			loadPath = "images" + loadPath;
			handled = loadContent(loadPath);
			setMime_type_("text/png");
		}

		if (handled) {
			// Indicate the headers are available.
			callback.Continue();
			return true;
		}

		System.out.println("LocalSchemaHandler Failed to respond to local resource: " + loadPath);

		return false;
	}
}
