package edu.kit.informatik.ragnarok.logic.levelcreator.parser;

import edu.kit.informatik.ragnarok.logic.levelcreator.Configurable;

public class SettingDividerParser {

	public SettingDividerParser(Configurable configurable, String input) {

		String[] settings = input.split(",");

		for (String setting : settings) {
			String trimmed = setting.trim();
			if (!trimmed.equals("")) {
				new SettingParser(configurable, trimmed);
			}

		}
	}
}
