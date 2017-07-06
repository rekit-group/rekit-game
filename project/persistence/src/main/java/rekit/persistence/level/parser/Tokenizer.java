package rekit.persistence.level.parser;

import java.util.StringTokenizer;

/**
 * This class realizes a tokenizer for the LevelLanguage.
 *
 * @author Dominik Fuchss
 * @see TokenType
 *
 */
public class Tokenizer {
	/**
	 * The original input string.
	 */
	private final String input;
	/**
	 * The inner tokenizer.
	 */
	private final StringTokenizer scanner;

	/**
	 * Instantiate a new Tokenizer by input string.
	 *
	 * @param input
	 *            the input string
	 */
	public Tokenizer(String input) {
		if (input == null) {
			throw new IllegalArgumentException("Null is no input!");
		}
		String modIn = "" + input;
		for (String k : TokenType.SPECIAL_ID_MAP_TO_TYPES.keySet()) {
			modIn = modIn.replace(k, " " + k + " ");
		}
		this.input = modIn;
		this.scanner = new StringTokenizer(this.input, " \t\n\r");
	}

	/**
	 * Get the next token.
	 *
	 * @return the token
	 */
	public Token nextToken() {
		if (!this.scanner.hasMoreTokens()) {
			return Token.getEOSToken();
		}
		return new Token(this.scanner.nextToken());
	}
}
