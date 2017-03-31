package rekit.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fuchss.configuration.parser.Parser;

import rekit.config.GameConf;
import rekit.primitives.geometry.Vec;

/**
 * This {@link Parser} is used for parsing {@link Vec Vectors}.
 *
 * @author Angelo Aracri
 *
 */
public final class VecParser implements Parser {
	@Override
	public Object parseIt(String definition, String[] path) throws Exception {

		Pattern pattern = Pattern.compile("[-|\\+]?([0-9]+\\.[0-9]+[f|F]),[-|\\+]?([0-9]+\\.[0-9]+[f|F])");
		Matcher matcher = pattern.matcher(definition);
		if (!matcher.find()) {
			GameConf.GAME_LOGGER.error("BundleHelper: " + definition + " is no Vec");
			return null;
		}

		float x = Float.parseFloat(matcher.group(1));
		float y = Float.parseFloat(matcher.group(2));

		return new Vec(x, y);

	}
}
