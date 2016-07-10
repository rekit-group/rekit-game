package edu.kit.informatik.ragnarok.logic.level.parser;

import java.util.ArrayList;
import java.util.Arrays;
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
	/** The look ahead Token. */
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
		System.out.println("BEGIN OF PARSING");
		this.lookAhead = this.tokenizer.nextToken();
		while (this.isToken(TokenType.SETTING) || this.isToken(TokenType.BOSS_SETTING) || this.isToken(TokenType.ALIAS)) {
			if (this.isToken(TokenType.ALIAS)) {
				this.parseAlias(manager);
			} else if (this.isToken(TokenType.SETTING)) {
				this.parseSetting(manager);
			} else {
				this.parseBossSetting(manager);
			}
		}
		while (this.isToken(TokenType.BEGIN)) {
			this.parseLevel(manager);
		}
		System.out.println("END OF PARSING");
		this.readToken(TokenType.EOS);
		this.reset();
	}

	private void parseLevel(StructureManager manager) {
		System.out.println("Begin parsing level part");
		this.readToken(TokenType.BEGIN);
		List<String[]> lines = new LinkedList<>();
		while (this.isToken(TokenType.BEGIN)) {
			this.readLevelLine(lines);
		}
		this.readToken(TokenType.END);
		Structure s = new Structure(lines, manager.getAlias());
		manager.addStructure(s);
		System.out.println("End parsing level part");

	}

	private void readLevelLine(List<String[]> lines) {
		this.readToken(TokenType.BEGIN);

		List<String> line = new ArrayList<>();

		while (!this.isToken(TokenType.END)) {
			line.add(this.readToken(TokenType.RAW).getValue());
		}
		this.readToken(TokenType.END);
		String[] res = new String[line.size()];
		int i = 0;
		for (String o : line) {
			res[i++] = o;
		}
		System.out.println("parsed line: " + Arrays.toString(res));
		lines.add(res);

	}

	private void parseAlias(StructureManager manager) {
		Token alias = this.readToken(TokenType.ALIAS);
		String[] mapping = alias.getValue().split("::")[1].split("->");
		manager.setAlias(mapping[0], mapping[1]);
		System.out.println("Set ALIAS " + Arrays.toString(mapping));
	}

	private void parseSetting(StructureManager manager) {
		Token setting = this.readToken(TokenType.SETTING);
		String[] mapping = setting.getValue().split("::")[1].split("->");
		manager.setSetting(mapping[0], mapping[1]);
		System.out.println("Set SETTING " + Arrays.toString(mapping));

	}

	private void parseBossSetting(StructureManager manager) {
		Token setting = this.readToken(TokenType.BOSS_SETTING);
		String[] mapping = setting.getValue().split("::")[1].split("->");
		manager.bossSettings.setSetting(mapping[0], mapping[1]);
		System.out.println("Set BOSS_SETTING " + Arrays.toString(mapping));

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
