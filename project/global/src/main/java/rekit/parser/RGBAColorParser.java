package rekit.parser;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fuchss.configuration.Configurable;
import org.fuchss.configuration.parser.Parser;

import rekit.primitives.image.RGBAColor;

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
		Pattern patternRGBA = Pattern.compile("([0-9]+),([0-9]+),([0-9]+),([0-9]+)");
		Matcher matcherRGBA = patternRGBA.matcher(definition);

		Pattern patternRGB = Pattern.compile("([0-9]+),([0-9]+),([0-9]+)");
		Matcher matcherRGB = patternRGB.matcher(definition);
		boolean rgba = matcherRGBA.matches();
		boolean rgb = matcherRGB.matches();
		if (!rgba && !rgb) {
			Parser.LOGGER.error(definition + " is not a RGB(A) color!");
			return false;
		}
		Matcher matcher = rgba ? matcherRGBA : matcherRGB;

		int r = Integer.parseInt(matcher.group(1));
		int g = Integer.parseInt(matcher.group(2));
		int b = Integer.parseInt(matcher.group(3));
		int a = rgba ? Integer.parseInt(matcher.group(4)) : 255;

		field.set(obj, new RGBAColor(r, g, b, a));
		return true;
	}
}
