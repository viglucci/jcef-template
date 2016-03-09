package com.viglucci.app.jade;

import de.neuland.jade4j.template.TemplateLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author Kevin
 * @since 12/21/2015
 */
public class ClasspathTemplateLoader implements TemplateLoader{

	private static final String suffix = ".jade";
	private String encoding = "UTF-8";
	private String base = "templates/jade/";

	public ClasspathTemplateLoader() {}

	public ClasspathTemplateLoader(String base) {
		this.setBase(base);
	}

	@Override
	public Reader getReader(String path) throws IOException {
		path = this.base + path;
		if(!path.endsWith(this.suffix))
			path = path + suffix;
		System.out.println("ClasspathTemplateLoader loading resource: " + path);
		return new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(path), getEncoding());
	}

	public void setBase(String base) {
		this.base = base;
	}

	public long getLastModified(String name) {
		return -1;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
