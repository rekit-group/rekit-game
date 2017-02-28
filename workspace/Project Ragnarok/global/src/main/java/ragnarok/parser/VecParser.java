package ragnarok.parser;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import home.fox.configuration.Configurable;
import home.fox.configuration.parser.Parser;
import ragnarok.config.GameConf;
import ragnarok.primitives.geometry.Vec;

/**
 * This {@link Parser} is used for parsing {@link Vec Vectors}.
 *
 * @author Angelo Aracri
 *
 */
public final class VecParser implements Parser {
	@Override
	public boolean parse(Configurable obj, Field field, String definition, String[] path) throws Exception {
		if (!Parser.super.parse(obj, field, definition, path)) {
			return false;
		}
		Pattern pattern = Pattern.compile("[-|\\+]?([0-9]+\\.[0-9]+[f|F]),[-|\\+]?([0-9]+\\.[0-9]+[f|F])");
		Matcher matcher = pattern.matcher(definition);
		if (!matcher.find()) {
			GameConf.GAME_LOGGER.error("BundleHelper: " + definition + " is no Vec");
			return false;
		}

		float x = Float.parseFloat(matcher.group(1));
		float y = Float.parseFloat(matcher.group(2));

		field.set(obj, new Vec(x, y));
		return true;
	}
}
