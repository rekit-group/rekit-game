package edu.kit.informatik.ragnarok.logic.level.parser.token;

import java.util.StringTokenizer;

public class Tokenizer {
	private final String input;
	private final StringTokenizer scanner;

	public Tokenizer(String input) {
		if (input == null) {
			throw new IllegalArgumentException("Null is no input!");
		}
		this.input = "" + input;
		this.scanner = new StringTokenizer(this.input.replace("{", " { ").replace("}", " } "), " \t\n\r");
	}

	public Token nextToken() {
		if (!this.scanner.hasMoreTokens()) {
			return new Token();
		}
		return new Token(this.scanner.nextToken());
	}
}
