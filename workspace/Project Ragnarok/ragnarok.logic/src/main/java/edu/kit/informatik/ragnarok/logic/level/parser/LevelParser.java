package edu.kit.informatik.ragnarok.logic.level.parser;

import edu.kit.informatik.ragnarok.logic.level.Level;
import edu.kit.informatik.ragnarok.logic.level.StructureManager;

/**
 * This class shall be implemented from all classes which want to parse a
 * {@link Level} to a {@link StructureManager}.
 *
 * @author Dominik Fuchß
 *
 */
public abstract class LevelParser {
	/**
	 * Get the default parser ({@link StringParser}).
	 *
	 * @param input
	 *            the input string
	 * @return the default parser
	 */
	public static LevelParser getParser(String input) {
		return new StringParser(input);
	}

	/**
	 * The original string.
	 */
	protected final String input;

	/**
	 * Set all necessary fields.
	 *
	 * @param input
	 *            the original string
	 */
	protected LevelParser(String input) {
		if (input == null) {
			throw new IllegalArgumentException("Input for LevelParser cannot be null");
		}
		this.input = "" + input;
	}

	/**
	 * Parse the level to the {@link StructureManager}.
	 *
	 * @param manager
	 *            the structure manager
	 */
	public abstract void parse(StructureManager manager);

}
