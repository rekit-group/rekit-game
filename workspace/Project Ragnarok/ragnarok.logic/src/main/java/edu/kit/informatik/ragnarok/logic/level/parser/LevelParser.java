package edu.kit.informatik.ragnarok.logic.level.parser;

import edu.kit.informatik.ragnarok.logic.level.StructureManager;

public abstract class LevelParser {
	public static LevelParser getParser(String input) {
		return new FileParser(input);
	}

	protected final String input;

	protected LevelParser(String input) {
		if (input == null) {
			throw new IllegalArgumentException("Input for LevelParser cannot be null");
		}
		this.input = "" + input;
	}

	public abstract void parse(StructureManager manager);

}
