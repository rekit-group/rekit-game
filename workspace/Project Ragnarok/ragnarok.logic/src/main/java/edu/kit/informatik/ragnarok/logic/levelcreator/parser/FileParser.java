package edu.kit.informatik.ragnarok.logic.levelcreator.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.kit.informatik.ragnarok.logic.levelcreator.StructureManager;

public class FileParser extends Parser {

	private static String n = System.getProperty("line.separator");
	private static String patternStr = "(?s)^\\{([0-9a-zA-Z,: ]+)\\}" + FileParser.n + "\\{([0-9a-zA-Z,: ]+)\\}" + FileParser.n + "" + FileParser.n
			+ "(.+)" + FileParser.n + "$";

	public FileParser(StructureManager employer, String input) {

		Pattern pattern = Pattern.compile(FileParser.patternStr);
		Matcher matcher = pattern.matcher(input);
		if (!matcher.find()) {
			System.err.println("StructureManager: " + input + " is no valid file");
			return;
		}

		new SettingDividerParser(employer, matcher.group(1));
		new SettingDividerParser(employer.bossSettings, matcher.group(2));
		new StructureDividerParser(employer, matcher.group(3));

	}
}
