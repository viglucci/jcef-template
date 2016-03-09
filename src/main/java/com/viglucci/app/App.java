package com.viglucci.app;

import com.viglucci.app.jade.Jade4J;
import org.cef.CefFrame;
import org.cef.OS;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kevin
 * @since 12/20/2015.
 */
public class App {

	public static void main(String[] args) {
		//template://index/index.jade
		CefFrame frame = new CefFrame("template://index/index.jade", OS.isLinux(), false, args);
		frame.setSize(800,600);
		frame.setVisible(true);

		Map<String, Object> model = new HashMap<String, Object>();

		try {
			String view = "index/index.jade";
			String html = Jade4J.render(view, model);
			System.out.println("Jade template load: " + view + ", " + html);
			frame.setContents(html, "template://" + view);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
