package com.viglucci.app;

import com.viglucci.app.jade.Jade4J;
import org.cef.CefFrame;
import org.cef.OS;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kevin
 * @since 12/20/2015.
 */
public class App {

	public static void main(String[] args) {
		CefFrame frame = new CefFrame("local:app/html/index.html", OS.isLinux(), false, args);
		frame.setSize(800,600);
		frame.setVisible(true);

		Map<String, Object> model = new HashMap<String, Object>();
		try {

			String view = "index.jade";
			String html = Jade4J.render(view, model);

			System.out.println(html);
			frame.setContents(html, "template://" + view);

			//byte[] byteArr = Jade4J.renderAsByteArray("index.jade", model);
			//System.out.println("debug");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
