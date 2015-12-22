package com.viglucci.app.util;

import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.model.JadeModel;
import de.neuland.jade4j.template.JadeTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Kevin
 * @since 12/21/2015
 */
public class TemplateHelper {

	public static ByteArrayOutputStream stringToOutputStream(String string) throws IOException{
		InputStream inputStream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
		ByteArrayOutputStream outFile = new ByteArrayOutputStream();
		int readByte = -1;
		while ((readByte = inputStream.read()) >= 0)
			outFile.write(readByte);
		return outFile;
	}

	public static String jadeTemplateToString(JadeTemplate template, Map<String, Object> model) throws JadeCompilerException {
		JadeModel jadeModel = new JadeModel(model);
		StringWriter writer = new StringWriter();
		template.process(jadeModel, writer);
		return writer.toString();
	}
}
