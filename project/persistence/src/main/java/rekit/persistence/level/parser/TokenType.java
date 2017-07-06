package rekit.persistence.level.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * This class defines the different TokenTypes which exist in the LevelLanguage.
 *
 * @author Dominik Fuchss
 *
 */
public enum TokenType {
	/**
	 * A normal setting for the level.
	 */
	SETTING,
	/**
	 * A setting which belongs to a boss.
	 */
	BOSS_SETTING,
	/**
	 * An Alias.
	 */
	ALIAS,
	/**
	 * Mapping.
	 */
	MAPPING,
	/**
	 * Begin of an Level or an LevelLine (equals "{").
	 */
	BEGIN,
	/**
	 * End of an Level or an LevelLine (equals "}").
	 */
	END,
	/**
	 * End of String Token.
	 */
	EOS,
	/**
	 * Token for RAW content.
	 */
	RAW,
	/**
	 * Token for an delimiter of an Setting or Alias (equals "::").
	 */
	DELIMITER;

	/** Map for Special Identifiers to Special TokenType. */
	private static final Map<String, TokenType> SPECIAL_ID_MAP_TO_TYPES = new HashMap<String, TokenType>() {
		private static final long serialVersionUID = -7109095209912302200L;

		{
			this.put("{", TokenType.BEGIN);
			this.put("}", TokenType.END);
			this.put("::", TokenType.DELIMITER);

		}
	};

	/**
	 * Calculate type.
	 *
	 * @param input
	 *            the input
	 * @return the token type
	 */
	public static TokenType calcType(String input) {
		TokenType type = TokenType.SPECIAL_ID_MAP_TO_TYPES.get(input);
		if (type == null) {
			return TokenType.determinateByContent(input);
		}
		return type;
	}

	/**
	 * Find value from content.
	 *
	 * @param input
	 *            the input
	 * @return the type
	 */
	private static TokenType determinateByContent(String input) {
		if (input.equals("#ALIAS")) {
			return TokenType.ALIAS;
		}
		if (input.equals("#BOSS_SETTING")) {
			return TokenType.BOSS_SETTING;
		}
		if (input.equals("#SETTING")) {
			return TokenType.SETTING;
		}
		if (input.matches("(\\w|(\\+|-)?\\d|_|\\.)+->(\\w|\\d|_|-|\\.|:)+")) {
			return TokenType.MAPPING;
		}
		return TokenType.RAW;
	}
}
