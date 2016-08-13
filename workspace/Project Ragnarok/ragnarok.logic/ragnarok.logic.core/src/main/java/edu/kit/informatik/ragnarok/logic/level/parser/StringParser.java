package edu.kit.informatik.ragnarok.logic.level.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.kit.informatik.ragnarok.logic.level.Level;
import edu.kit.informatik.ragnarok.logic.level.Structure;
import edu.kit.informatik.ragnarok.logic.level.StructureManager;
import edu.kit.informatik.ragnarok.logic.level.parser.token.Token;
import edu.kit.informatik.ragnarok.logic.level.parser.token.TokenType;
import edu.kit.informatik.ragnarok.logic.level.parser.token.Tokenizer;
import edu.kit.informatik.ragnarok.logic.level.parser.token.UnexpectedTokenException;

/**
 * This class directly parses an {@link Level} from an {@link String} to a
 * {@link StructureManager}
 *
 * @author Dominik Fuchß
 *
 */
class StringParser extends LevelParser {
	/**
	 * The tokenizer
	 */
	private Tokenizer tokenizer;
	/**
	 * The look ahead Token
	 */
	private Token lookAhead;

	/**
	 * Instantiate the parser by the input string
	 *
	 * @param input
	 */
	StringParser(String input) {
		super(input);
		this.tokenizer = new Tokenizer(this.input);
	}

	@Override
	public void parse(StructureManager manager) {
		if (manager == null) {
			throw new IllegalArgumentException("manager cannot be null");
		}
		this.lookAhead = this.tokenizer.nextToken();
		this.parseStructure(manager);
		this.readToken(TokenType.EOS);
		this.reset();
	}

	private void parseStructure(StructureManager manager) {
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
			this.parseLevel(manager);
		}
		if (!this.isToken(TokenType.EOS)) {
			this.parseStructure(manager);
		}
	}

	private void parseLevel(StructureManager manager) {
		this.readToken(TokenType.BEGIN);
		List<String[]> lines = new LinkedList<>();
		while (this.isToken(TokenType.BEGIN)) {
			this.readLevelLine(lines);
		}
		this.readToken(TokenType.END);
		Structure s = new Structure(lines, manager.getAlias());
		manager.addStructure(s);
	}

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

	private void parseAlias(StructureManager manager) {
		this.readToken(TokenType.ALIAS);
		this.readToken(TokenType.DELIMITER);
		String[] mapping = this.parseMapping();
		manager.setAlias(mapping[0], mapping[1]);
	}

	private void parseSetting(StructureManager manager) {
		this.readToken(TokenType.SETTING);
		this.readToken(TokenType.DELIMITER);
		String[] mapping = this.parseMapping();
		manager.setSetting(mapping[0], mapping[1]);
	}

	private void parseBossSetting(StructureManager manager) {
		this.readToken(TokenType.BOSS_SETTING);
		this.readToken(TokenType.DELIMITER);
		String[] mapping = this.parseMapping();
		manager.bossSettings.setSetting(mapping[0], mapping[1]);
	}

	private String[] parseMapping() {
		return this.readToken(TokenType.MAPPING).getValue().split("->");
	}

	private void reset() {
		this.tokenizer = new Tokenizer(this.input);
		this.lookAhead = null;
	}

	/**
	 * Checks if is token.
	 *
	 * @param type
	 *            the type
	 * @return true, if is token
	 */
	private boolean isToken(TokenType type) {
		return this.lookAhead.getType() == type;
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
