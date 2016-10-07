package edu.kit.informatik.ragnarok.logic.level.parser.token;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a token for the LevelLanguage.
 *
 * @author Dominik Fuchß
 * @see TokenType
 */
public class Token {

	/** Map for Special Identifiers to Special TokenType. */
	private static final Map<String, TokenType> SPECIAL_ID_MAP_TO_TYPES = new HashMap<String, TokenType>() {
		/**
		 * UID
		 */
		private static final long serialVersionUID = -7109095209912302200L;

		{
			this.put("{", TokenType.BEGIN);
			this.put("}", TokenType.END);
			this.put("::", TokenType.DELIMITER);

		}
	};

	/** The type of the Token. */
	private TokenType type;

	/** The value of the Token. */
	private String value;

	/**
	 * This creates a new Token by Value.
	 *
	 * @param value
	 *            value
	 */
	public Token(String value) {
		this.value = value;
		this.type = this.calcType(value);
	}

	/**
	 * This creates an EOS token.
	 */
	public Token() {
		this.value = null;
		this.type = TokenType.EOS;
	}

	/**
	 * Calculate type.
	 *
	 * @param input
	 *            the input
	 * @return the token type
	 */
	private TokenType calcType(String input) {
		TokenType type = Token.SPECIAL_ID_MAP_TO_TYPES.get(input);
		if (type == null) {
			return this.determinateByContent(input);
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
	private TokenType determinateByContent(String input) {
		if (input.equals("#ALIAS")) {
			return TokenType.ALIAS;
		}
		if (input.equals("#BOSS_SETTING")) {
			return TokenType.BOSS_SETTING;
		}
		if (input.equals("#SETTING")) {
			return TokenType.SETTING;
		}
		if (input.matches("(\\w|(\\+|-)?\\d|\\.)+->(\\w|\\d|\\.)+")) {
			return TokenType.MAPPING;
		}
		return TokenType.RAW;
	}

	/**
	 * Get the TokenType.
	 *
	 * @return the Type
	 */
	public TokenType getType() {
		return this.type;
	}

	/**
	 * Get the Token Value.
	 *
	 * @return the Value
	 */
	public String getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return "{Type: " + this.type + " Value: " + this.value + "}";
	}
}
