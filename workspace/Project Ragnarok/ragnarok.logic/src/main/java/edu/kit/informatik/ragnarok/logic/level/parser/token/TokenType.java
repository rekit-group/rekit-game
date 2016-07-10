package edu.kit.informatik.ragnarok.logic.level.parser.token;

/**
 * This class defines the different TokenTypes which exist in the LevelLanguage
 *
 * @author Dominik Fuchß
 *
 */
public enum TokenType {
	/**
	 * A normal setting for the level
	 */
	SETTING,
	/**
	 * A setting which belongs to a boss
	 */
	BOSS_SETTING,
	/**
	 * An Alias
	 */
	ALIAS,
	/**
	 * Begin of an Level or an LevelLine (equals "{")
	 */
	BEGIN,
	/**
	 * End of an Level or an LevelLine (equals "}")
	 */
	END,
	/**
	 * End of String Token
	 */
	EOS,
	/**
	 * Token for RAW content
	 */
	RAW
}
