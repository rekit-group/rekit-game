package edu.kit.informatik.ragnarok.logic.levelcreator.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.kit.informatik.ragnarok.logic.levelcreator.StructureManager;

public class FileParser extends Parser {
	public FileParser(StructureManager employer, String input) {

		// TODO does this last \n cause troubles on other systems?
		Pattern pattern = Pattern.compile("(?s)^\\{([0-9a-zA-Z,: ]+)\\}\n\\{([0-9a-zA-Z,: ]+)\\}\n\n(.+)\n$");
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
