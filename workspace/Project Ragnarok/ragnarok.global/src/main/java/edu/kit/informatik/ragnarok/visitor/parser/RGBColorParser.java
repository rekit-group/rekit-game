package edu.kit.informatik.ragnarok.visitor.parser;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import edu.kit.informatik.ragnarok.visitor.Visitable;

/**
 * This {@link Parser} is used for parsing {@link RGBColor RGBColors}.
 *
 * @author Dominik Fuchß
 *
 */
public final class RGBColorParser implements Parser {
	@Override
	public boolean parse(Visitable obj, Field field, String definition) throws Exception {
		if (!Parser.super.parse(obj, field, definition)) {
			return false;
		}
		Pattern pattern = Pattern.compile("([0-9]+),([0-9]+),([0-9]+)");
		Matcher matcher = pattern.matcher(definition);
		if (!matcher.find()) {
			System.err.println("RGBColorParser: " + definition + " is no RBG");
			return false;
		}

		int r = Integer.parseInt(matcher.group(1));
		int g = Integer.parseInt(matcher.group(2));
		int b = Integer.parseInt(matcher.group(3));

		field.set(obj, new RGBColor(r, g, b));
		return true;
	}
}
