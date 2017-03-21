package rekit.persistence.level.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rekit.persistence.level.LevelDefinition;
import rekit.persistence.level.parser.token.Token;
import rekit.persistence.level.parser.token.TokenType;
import rekit.persistence.level.parser.token.Tokenizer;
import rekit.persistence.level.parser.token.UnexpectedTokenException;

/**
 * This class shall be implemented from all classes which want to parse a
 * {@link LevelDefinition} to a {@link StructureManager}.
 *
 * @author Dominik Fuchss
 *
 */
public final class LevelParser {
	/**
	 * Parse a level.
	 *
	 * @param input
	 *            the definitions
	 * @param manager
	 *            the manager of structure
	 */
	public static final void parseLevel(String input, LevelDefinition manager) {
		new LevelParser(input).parse(manager);
	}

	/**
	 * The original string.
	 */
	private final String input;

	/**
	 * The tokenizer.
	 */
	private Tokenizer tokenizer;
	/**
	 * The look ahead Token.
	 */
	private Token lookAhead;

	/**
	 * Instantiate the parser by the input string.
	 *
	 * @param input
	 *            the input string
	 */
	private LevelParser(String input) {
		if (input == null) {
			throw new IllegalArgumentException("Input for LevelParser cannot be null");
		}
		this.input = "" + input;
		this.tokenizer = new Tokenizer(this.input);
	}

	/**
	 * Parse the level to the {@link StructureManager}.
	 *
	 * @param manager
	 *            the structure manager
	 */
	public void parse(LevelDefinition manager) {
		if (manager == null) {
			throw new IllegalArgumentException("manager cannot be null");
		}
		this.lookAhead = this.tokenizer.nextToken();
		this.parseLevel(manager);
		this.readToken(TokenType.EOS);
		manager.finish();
		this.reset();
	}

	/**
	 * Parse a {@link StructureDefinition}.
	 *
	 * @param manager
	 *            the manager
	 */
	private void parseLevel(LevelDefinition manager) {
		if (this.isToken(TokenType.ALIAS)) {
			this.parseAlias(manager);
		}
		if (this.isToken(TokenType.SETTING)) {
			this.parseSetting(manager);
		}
		if (this.isToken(TokenType.BOSS_SETTING)) {
			this.parseBossSetting(manager);
		}
		if (this.isToken(TokenType.BEGIN)) {
			this.parseStructure(manager);
		}
		if (this.isToken(TokenType.ALIAS, TokenType.SETTING, TokenType.BOSS_SETTING, TokenType.BEGIN)) {
			this.parseLevel(manager);
		}
	}

	/**
	 * Parse a {@link LevelDefinition}.
	 *
	 * @param manager
	 *            the manager
	 */
	private void parseStructure(LevelDefinition manager) {
		this.readToken(TokenType.BEGIN);
		List<String[]> lines = new LinkedList<>();
		while (this.isToken(TokenType.BEGIN)) {
			this.readLevelLine(lines);
		}
		this.readToken(TokenType.END);
		manager.addStructure(lines);
	}

	/**
	 * Read / Parse level lines.
	 *
	 * @param lines
	 *            the lines
	 */
	private void readLevelLine(List<String[]> lines) {
		this.readToken(TokenType.BEGIN);

		List<String> line = new ArrayList<>();

		while (!this.isToken(TokenType.END)) {
			line.add(this.readToken(TokenType.RAW).getValue());
		}
		this.readToken(TokenType.END);
		String[] res = new String[line.size()];
		line.toArray(res);
		lines.add(res);
	}

	/**
	 * Parse an alias.
	 *
	 * @param manager
	 *            the manager
	 */
	private void parseAlias(LevelDefinition manager) {
		this.readToken(TokenType.ALIAS);
		this.readToken(TokenType.DELIMITER);
		String[] mapping = this.parseMapping();
		manager.setAlias(mapping[0], mapping[1]);
	}

	/**
	 * Parse a setting.
	 *
	 * @param manager
	 *            the manager
	 */
	private void parseSetting(LevelDefinition manager) {
		this.readToken(TokenType.SETTING);
		this.readToken(TokenType.DELIMITER);
		String[] mapping = this.parseMapping();
		manager.setSetting(mapping[0], mapping[1]);
	}

	/**
	 * Parse an boss setting.
	 *
	 * @param manager
	 *            the manager
	 */
	private void parseBossSetting(LevelDefinition manager) {
		this.readToken(TokenType.BOSS_SETTING);
		this.readToken(TokenType.DELIMITER);
		String[] mapping = this.parseMapping();
		manager.setBossSetting(mapping[0], mapping[1]);
	}

	/**
	 * Parse a mapping.
	 *
	 * @return String[0] -> String[1]
	 */
	private String[] parseMapping() {
		return this.readToken(TokenType.MAPPING).getValue().split("->");
	}

	/**
	 * Reset parser.
	 */
	private void reset() {
		this.tokenizer = new Tokenizer(this.input);
		this.lookAhead = null;
	}

	/**
	 * Checks if is token of one of the types.
	 *
	 * @param types
	 *            the types
	 * @return true, if is token
	 */
	private boolean isToken(TokenType... types) {
		TokenType type = this.lookAhead.getType();
		for (TokenType t : types) {
			if (type == t) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Read token.
	 *
	 * @param type
	 *            the type
	 * @return the token
	 */
	private Token readToken(TokenType type) {
		if (!this.isToken(type)) {
			throw new UnexpectedTokenException(this.lookAhead, type);
		}
		Token token = this.lookAhead;
		this.lookAhead = this.tokenizer.nextToken();
		return token;

	}

}
