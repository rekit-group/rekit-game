package edu.kit.informatik.ragnarok.parser;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.primitives.image.RGBColor;
import home.fox.visitors.Visitable;
import home.fox.visitors.annotations.ClassParser;
import home.fox.visitors.parser.Parser;

/**
 * This {@link Parser} is used for parsing {@link RGBColor RGBColors}.
 *
 * @author Dominik Fuchss
 *
 */
@ClassParser(RGBColorParser.class)
public final class RGBColorParser implements Parser {
	@Override
	public boolean parse(Visitable obj, Field field, String definition, String[] path) throws Exception {
		if (!Parser.super.parse(obj, field, definition, path)) {
			return false;
		}
		Pattern pattern = Pattern.compile("([0-9]+),([0-9]+),([0-9]+)");
		Matcher matcher = pattern.matcher(definition);
		if (!matcher.find()) {
			GameConf.GAME_LOGGER.error("RGBColorParser: " + definition + " is no RBG");
			return false;
		}

		int r = Integer.parseInt(matcher.group(1));
		int g = Integer.parseInt(matcher.group(2));
		int b = Integer.parseInt(matcher.group(3));

		field.set(obj, new RGBColor(r, g, b));
		return true;
	}
}
