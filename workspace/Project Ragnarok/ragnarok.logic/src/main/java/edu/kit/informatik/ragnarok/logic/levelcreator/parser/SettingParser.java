package edu.kit.informatik.ragnarok.logic.levelcreator.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.kit.informatik.ragnarok.logic.levelcreator.StructureManager;

public class SettingParser {
	public SettingParser(StructureManager employer, String input) {
		Pattern pattern = Pattern.compile("^([0-9a-zA-Z]+):([false|true]+)$");
		Matcher matcher = pattern.matcher(input);
		if (!matcher.find()) {
			System.err.println("StructureManager: " + input + " is no valid setting declaration");
			return;
		}

		employer.setSetting(matcher.group(1), matcher.group(2).equals("true") ? true : false);
	}
}
