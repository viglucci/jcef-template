package com.viglucci.app.jade;

import com.viglucci.app.util.TemplateHelper;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.expression.ExpressionHandler;
import de.neuland.jade4j.expression.JexlExpressionHandler;
import de.neuland.jade4j.parser.Parser;
import de.neuland.jade4j.parser.node.Node;
import de.neuland.jade4j.template.JadeTemplate;
import de.neuland.jade4j.template.TemplateLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author Kevin
 * @since 12/21/2015
 */
public class Jade4J extends de.neuland.jade4j.Jade4J {

	public static String render(String filename, Map<String, Object> model) throws IOException, JadeCompilerException {
		return render(filename, model, false);
	}

	public static String render(String filename, Map<String, Object> model, boolean pretty) throws IOException, JadeCompilerException {
		JadeTemplate template = getTemplate(filename);
		template.setPrettyPrint(pretty);
		return TemplateHelper.jadeTemplateToString(template, model);
	}

	public static byte[] renderAsByteArray(String filename, Map<String, Object> model) throws IOException, JadeCompilerException {
		return renderAsByteArray(filename, model, false);
	}

	public static byte[] renderAsByteArray(String filename, Map<String, Object> model, boolean pretty) throws IOException, JadeCompilerException {
		JadeTemplate template = getTemplate(filename);
		template.setPrettyPrint(pretty);
		String string = TemplateHelper.jadeTemplateToString(template, model);
		return TemplateHelper.stringToOutputStream(string).toByteArray();
	}

	public static JadeTemplate getTemplate(String filename) throws IOException {
		return createTemplate(filename, new ClasspathTemplateLoader(), new JexlExpressionHandler());
	}

	private static JadeTemplate createTemplate(String filename, TemplateLoader loader, ExpressionHandler expressionHandler) throws IOException {
		Parser parser = new Parser(filename, loader,expressionHandler);
		Node root = parser.parse();
		JadeTemplate template = new JadeTemplate();
		template.setExpressionHandler(expressionHandler);
		template.setTemplateLoader(loader);
		template.setRootNode(root);
		return template;
	}
}
