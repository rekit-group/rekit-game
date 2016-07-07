package edu.kit.informatik.ragnarok.logic.level.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.kit.informatik.ragnarok.logic.level.Configurable;

public class SettingParser {
	public SettingParser(Configurable configurable, String input) {
		Pattern pattern = Pattern.compile("^([0-9a-zA-Z]+):([false|true|[0-9]]+)$");
		Matcher matcher = pattern.matcher(input);
		if (!matcher.find()) {
			System.err.println("StructureManager: " + input + " is no valid setting declaration");
			return;
		}

		int value = 0;

		if (matcher.group(2).matches("^true|false$")) {
			value = matcher.group(2).equals("true") ? 1 : 0;
		} else {
			value = Integer.parseInt(matcher.group(2));
		}

		configurable.setSetting(matcher.group(1), value);
	}
}
