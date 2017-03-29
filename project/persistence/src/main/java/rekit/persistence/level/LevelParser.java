package rekit.persistence.level;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rekit.persistence.level.token.Token;
import rekit.persistence.level.token.TokenType;
import rekit.persistence.level.token.Tokenizer;
import rekit.persistence.level.token.UnexpectedTokenException;

/**
 * This class shall be implemented from all classes which want to parse a
 * {@link LevelDefinition}.
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
	 * Parse the level to the LevelDefinitionImpl.
	 *
	 * @param definition
	 *            the LevelDefinitionImpl
	 */
	public void parse(LevelDefinition definition) {
		if (definition == null) {
			throw new IllegalArgumentException("manager cannot be null");
		}
		this.lookAhead = this.tokenizer.nextToken();
		this.parseLevel(definition);
		this.readToken(TokenType.EOS);
		this.reset();
	}

	/**
	 * Parse a LevelDefinitionImpl.
	 *
	 * @param definition
	 *            LevelDefinitionImpl
	 */
	private void parseLevel(LevelDefinition definition) {
		if (this.isToken(TokenType.ALIAS)) {
			this.parseAlias(definition);
		}
		if (this.isToken(TokenType.SETTING)) {
			this.parseSetting(definition);
		}
		if (this.isToken(TokenType.BOSS_SETTING)) {
			this.parseBossSetting(definition);
		}
		if (this.isToken(TokenType.BEGIN)) {
			this.parseStructure(definition);
		}
		if (this.isToken(TokenType.ALIAS, TokenType.SETTING, TokenType.BOSS_SETTING, TokenType.BEGIN)) {
			this.parseLevel(definition);
		}
	}

	/**
	 * Parse a Structure.
	 *
	 * @param definition
	 *            the LevelDefinitionImpl
	 */
	private void parseStructure(LevelDefinition definition) {
		this.readToken(TokenType.BEGIN);
		List<String[]> lines = new LinkedList<>();
		while (this.isToken(TokenType.BEGIN)) {
			this.readLevelLine(lines);
		}
		this.readToken(TokenType.END);
		definition.addStructure(lines);
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
	 * @param definition
	 *            the LevelDefinitionImpl
	 */
	private void parseAlias(LevelDefinition definition) {
		this.readToken(TokenType.ALIAS);
		this.readToken(TokenType.DELIMITER);
		Token mp = this.lookAhead;
		String[] mapping = this.parseMapping();
		if (!mapping[0].matches("(-|\\+)?[0-9]+")) {
			throw new UnexpectedTokenException(mp, "alias must be a mapping from number");
		}
		definition.setAlias(mapping[0], mapping[1]);
	}

	/**
	 * Parse a setting.
	 *
	 * @param definition
	 *            the LevelDefinitionImpl
	 */
	private void parseSetting(LevelDefinition definition) {
		this.readToken(TokenType.SETTING);
		this.readToken(TokenType.DELIMITER);
		Token tk = this.lookAhead;
		String[] mapping = this.parseMapping();
		SettingKey key = SettingKey.getByString(mapping[0]);
		if (key == null) {
			throw new UnexpectedTokenException(tk, "Unknown SettingKey");
		}
		definition.setSetting(key, mapping[1]);
	}

	/**
	 * Parse an boss setting.
	 *
	 * @param definition
	 *            the LevelDefinitionImpl
	 */
	private void parseBossSetting(LevelDefinition definition) {
		this.readToken(TokenType.BOSS_SETTING);
		this.readToken(TokenType.DELIMITER);
		String[] mapping = this.parseMapping();
		definition.setBossSetting(mapping[0], mapping[1]);
	}

	/**
	 * Parse a mapping.
	 *
	 * @return String[0] -&gt; String[1]
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
