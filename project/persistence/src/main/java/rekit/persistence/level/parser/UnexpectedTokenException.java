package rekit.persistence.level.parser;

/**
 * The Class UnexpectedTokenExecption.<br>
 * This exception will thrown when an Unexpected Token reached
 *
 * @author Dominik Fuchss
 */
public class UnexpectedTokenException extends RuntimeException {

	/** UID. */
	private static final long serialVersionUID = 4789321964295484155L;

	/**
	 * Instantiates a new unexpected token execption.
	 *
	 * @param foundToken
	 *            the unexpected Token
	 * @param expectedToken
	 *            the expected Type
	 */
	public UnexpectedTokenException(Token foundToken, TokenType expectedToken) {

		super("Invalid Token: " + foundToken + " expected Type: " + expectedToken);

	}

	/**
	 * Instantiates a new unexpected token execption.
	 *
	 * @param foundToken
	 *            the unexpected Token
	 * @param comment
	 *            your comment
	 */
	public UnexpectedTokenException(Token foundToken, String comment) {
		super("Invalid Token: " + foundToken + " expected: " + comment);
	}
}
