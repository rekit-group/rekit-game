package edu.kit.informatik.ragnarok.logic.level.parser;

import edu.kit.informatik.ragnarok.logic.level.StructureManager;

public class StructureDividerParser {
	public StructureDividerParser(StructureManager employer, String input) {
		String[] settings = input.split(System.lineSeparator() + System.lineSeparator());

		for (String setting : settings) {
			new StructureParser(employer, setting);
		}
	}
}
