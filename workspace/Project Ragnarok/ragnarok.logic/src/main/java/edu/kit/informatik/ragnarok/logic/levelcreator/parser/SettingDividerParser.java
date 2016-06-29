package edu.kit.informatik.ragnarok.logic.levelcreator.parser;

import edu.kit.informatik.ragnarok.logic.levelcreator.StructureManager;

public class SettingDividerParser {

	public SettingDividerParser(StructureManager employer, String input) {
		String[] settings = input.split(", ");

		for (String setting : settings) {
			new SettingParser(employer, setting);
		}
	}
}
