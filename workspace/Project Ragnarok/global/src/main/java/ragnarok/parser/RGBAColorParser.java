package ragnarok.parser;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.parser.Parser;

import ragnarok.config.GameConf;
import ragnarok.primitives.image.RGBAColor;

/**
 * This {@link Parser} is used for parsing {@link RGBAColor RGBAColors}.
 *
 * @author Dominik Fuchss
 *
 */
public final class RGBAColorParser implements Parser {
	@Override
	public boolean parse(Configurable obj, Field field, String definition, String[] path) throws Exception {
		if (!Parser.super.parse(obj, field, definition, path)) {
			return false;
		}
		Pattern pattern = Pattern.compile("([0-9]+),([0-9]+),([0-9]+),([0-9]+)");
		Matcher matcher = pattern.matcher(definition);
		if (!matcher.find()) {
			GameConf.GAME_LOGGER.error("RGBColorParser: " + definition + " is no RBGA");
			return false;
		}

		int r = Integer.parseInt(matcher.group(1));
		int g = Integer.parseInt(matcher.group(2));
		int b = Integer.parseInt(matcher.group(3));
		int a = Integer.parseInt(matcher.group(3));

		field.set(obj, new RGBAColor(r, g, b, a));
		return true;
	}
}
