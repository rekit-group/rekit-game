package rekit.persistence.level.token;

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
		this.input = "" + input;
		this.scanner = new StringTokenizer(this.input.replace("::", " :: ").replace("{", " { ").replace("}", " } "), " \t\n\r");
	}

	/**
	 * Get the next token.
	 *
	 * @return the token
	 */
	public Token nextToken() {
		if (!this.scanner.hasMoreTokens()) {
			// return EOS Token
			return new Token();
		}
		return new Token(this.scanner.nextToken());
	}
}
