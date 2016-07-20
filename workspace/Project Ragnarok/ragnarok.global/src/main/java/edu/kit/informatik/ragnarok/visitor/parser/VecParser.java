package edu.kit.informatik.ragnarok.visitor.parser;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.visitor.Visitable;

/**
 * This {@link Parser} is used for parsing {@link Vec Vectors}
 * 
 * @author Angelo Aracri
 *
 */
public class VecParser implements Parser {
	@Override
	public boolean parse(Visitable obj, Field field, String definition) throws Exception {
		if (!Parser.super.parse(obj, field, definition)) {
			return false;
		}
		Pattern pattern = Pattern.compile("[-|\\+]?([0-9]+\\.[0-9]+[f|F]);[-|\\+]?([0-9]+\\.[0-9]+[f|F])");
		Matcher matcher = pattern.matcher(definition);
		if (!matcher.find()) {
			System.err.println("BundleHelper: " + definition + " is no Vec");
			return false;
		}

		float x = Float.parseFloat(matcher.group(1));
		float y = Float.parseFloat(matcher.group(2));
		
		field.set(obj, new Vec(x, y));
		return true;
	}
}
