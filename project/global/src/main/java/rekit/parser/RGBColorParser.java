package rekit.parser;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.parser.Parser;

import rekit.config.GameConf;
import rekit.primitives.image.RGBColor;

/**
 * This {@link Parser} is used for parsing {@link RGBColor RGBColors}.
 *
 * @author Dominik Fuchss
 *
 */
public final class RGBColorParser implements Parser {
	@Override
	public boolean parse(Configurable obj, Field field, String definition, String[] path) throws Exception {
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
