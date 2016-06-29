package edu.kit.informatik.ragnarok.logic.levelcreator.parser;

import edu.kit.informatik.ragnarok.logic.levelcreator.StructureManager;

public class StructureDividerParser {
	public StructureDividerParser(StructureManager employer, String input) {
		String[] settings = input.split("\n\n");

		for (String setting : settings) {
			new StructureParser(employer, setting);
		}
	}
}
