package rekit.persistence.level.parser;

/**
 * This class represents a token for the LevelLanguage.
 *
 * @author Dominik Fuchss
 * @see TokenType
 */
public class Token {

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
		this.type = TokenType.calcType(value);
	}

	private Token(String value, TokenType type) {
		this.value = value;
		this.type = type;
	}

	/**
	 * This creates an EOS token.
	 * 
	 * @return an EOS token
	 */
	public static Token getEOSToken() {
		return new Token(null, TokenType.EOS);
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
