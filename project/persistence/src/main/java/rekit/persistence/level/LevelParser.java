package rekit.persistence.level;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rekit.persistence.level.parser.Token;
import rekit.persistence.level.parser.TokenType;
import rekit.persistence.level.parser.Tokenizer;
import rekit.persistence.level.parser.UnexpectedTokenException;

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
	public static void parseLevel(String input, LevelDefinition manager) {
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
	 * Used to build a real Level from a representation in a file
	 */
	private LevelDefinition levelDef;

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
		this.levelDef = definition;
		this.lookAhead = this.tokenizer.nextToken();
		this.parseLevel();
		this.readToken(TokenType.EOS);
		this.reset();
	}

	/**
	 * Parse a LevelDefinitionImpl.
	 *
	 * @param definition
	 *            LevelDefinitionImpl
	 */
	private void parseLevel() {
		if (this.isToken(TokenType.ALIAS)) {
			this.parseAlias();
		}
		if (this.isToken(TokenType.SETTING)) {
			this.parseSetting();
		}
		if (this.isToken(TokenType.BOSS_SETTING)) {
			this.parseBossSetting();
		}
		if (this.isToken(TokenType.BEGIN)) {
			this.parseStructure();
		}
		if (this.isToken(TokenType.ALIAS, TokenType.SETTING, TokenType.BOSS_SETTING, TokenType.BEGIN)) {
			this.parseLevel();
		}
	}

	/**
	 * Parse a Structure.
	 *
	 * @param definition
	 *            the LevelDefinitionImpl
	 */
	private void parseStructure() {
		this.readToken(TokenType.BEGIN);
		List<String[]> lines = new LinkedList<>();
		while (this.isToken(TokenType.BEGIN)) {
			String[] line = this.readLevelLine();
			lines.add(line);
		}
		this.readToken(TokenType.END);
		this.levelDef.addStructure(lines);
	}

	/**
	 * Read / Parse level lines.
	 *
	 * @return a line of a level represented as a String[]
	 *
	 */
	private String[] readLevelLine() {
		List<String> line = new ArrayList<>();

		this.readToken(TokenType.BEGIN);
		while (!this.isToken(TokenType.END)) {
			line.add(this.readToken(TokenType.RAW).getValue());
		}
		this.readToken(TokenType.END);

		String[] res = new String[line.size()];
		return line.toArray(res);
	}

	/**
	 * Parse an alias.
	 *
	 * @param definition
	 *            the LevelDefinitionImpl
	 */
	private void parseAlias() {
		this.readToken(TokenType.ALIAS);
		this.readToken(TokenType.DELIMITER);
		Token mp = this.lookAhead;
		String[] mapping = this.parseMapping();
		if (!mapping[0].matches("(-|\\+)?[0-9]+")) {
			throw new UnexpectedTokenException(mp, "alias must be a mapping from number");
		}
		this.levelDef.setAlias(mapping[0], mapping[1]);
	}

	/**
	 * Parse a setting.
	 *
	 * @param definition
	 *            the LevelDefinitionImpl
	 */
	private void parseSetting() {
		this.readToken(TokenType.SETTING);
		this.readToken(TokenType.DELIMITER);
		Token tk = this.lookAhead;
		String[] mapping = this.parseMapping();
		SettingKey key = SettingKey.getByString(mapping[0]);
		if (key == null) {
			throw new UnexpectedTokenException(tk, "Unknown SettingKey");
		}
		this.levelDef.setSetting(key, mapping[1]);
	}

	/**
	 * Parse an boss setting.
	 *
	 * @param definition
	 *            the LevelDefinitionImpl
	 */
	private void parseBossSetting() {
		this.readToken(TokenType.BOSS_SETTING);
		this.readToken(TokenType.DELIMITER);
		String[] mapping = this.parseMapping();
		this.levelDef.setBossSetting(mapping[0], mapping[1]);
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
