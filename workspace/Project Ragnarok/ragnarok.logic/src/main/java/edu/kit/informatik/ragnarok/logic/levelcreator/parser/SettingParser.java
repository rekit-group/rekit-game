package edu.kit.informatik.ragnarok.logic.levelcreator.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.kit.informatik.ragnarok.logic.levelcreator.Configurable;

public class SettingParser {
	public SettingParser(Configurable configurable, String input) {
		Pattern pattern = Pattern.compile("^([0-9a-zA-Z]+):([false|true|0-9]+)$");
		Matcher matcher = pattern.matcher(input);
		if (!matcher.find()) {
			System.err.println("StructureManager: " + input + " is no valid setting declaration");
			return;
		}

		configurable.setSetting(matcher.group(1), matcher.group(2).equals("true") ? true : false);
	}
}
