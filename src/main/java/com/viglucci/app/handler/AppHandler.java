package com.viglucci.app.handler;

import org.cef.CefApp;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefSchemeHandlerFactory;
import org.cef.callback.CefSchemeRegistrar;
import org.cef.handler.CefAppHandlerAdapter;
import org.cef.handler.CefResourceHandler;
import org.cef.network.CefRequest;

/**
 * @author Kevin
 * @since 12/20/2015.
 */
public class AppHandler extends CefAppHandlerAdapter {

	public AppHandler(String[] args) {
		super(args);
	}

	@Override
	public void onRegisterCustomSchemes(CefSchemeRegistrar registrar) {
		if (registrar.addCustomScheme(LocalSchemaHandler.scheme, true, false, false))
			System.out.println("Added scheme " + LocalSchemaHandler.scheme + "://");
	}

	@Override
	public void onContextInitialized() {
		CefApp cefApp = CefApp.getInstance();
		cefApp.registerSchemeHandlerFactory(LocalSchemaHandler.scheme, LocalSchemaHandler.domain, new SchemeHandlerFactory());
	}

	private class SchemeHandlerFactory implements CefSchemeHandlerFactory {

		@Override
		public CefResourceHandler create(CefBrowser browser, String schemeName, CefRequest request) {
			if (schemeName.equals(LocalSchemaHandler.scheme))
				return new LocalSchemaHandler();
			return null;
		}
	}

	@Override
	public void stateHasChanged(CefApp.CefAppState state) {
		System.out.println("CefApp: "+state);
		if (state == CefApp.CefAppState.TERMINATED)
			System.exit(0);
	}
}
