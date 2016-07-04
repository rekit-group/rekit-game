package edu.kit.informatik.ragnarok.logic.levelcreator.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.kit.informatik.ragnarok.logic.levelcreator.Structure;
import edu.kit.informatik.ragnarok.logic.levelcreator.StructureManager;

public class StructureParser {

	private static String n = System.getProperty("line.separator");
	private static String patternStr = "(?s)^\\{([0-9a-zA-Z,: ]*)\\}" + StructureParser.n + "(.+)$";

	public StructureParser(StructureManager employer, String input) {

		Pattern pattern = Pattern.compile(StructureParser.patternStr);
		Matcher matcher = pattern.matcher(input);
		if (!matcher.find()) {
			System.err.println("StructureManager: " + input + " is no valid Structure");
			return;
		}

		int[][] result;

		// get all lines of elements
		String[] lines = matcher.group(2).split("\n");
		result = new int[lines.length][];

		for (int y = 0; y < lines.length; y++) {

			// get all elements of this line
			String[] elements = lines[y].split("\t");
			result[y] = new int[elements.length];

			// construct every char into array
			for (int x = 0; x < elements.length; x++) {
				result[y][x] = Integer.parseInt(elements[x].trim());
			}
		}

		// create new structure
		Structure structure = new Structure(result);
		// add settings
		new SettingDividerParser(structure, matcher.group(1));
		// add structure to Manager
		employer.addStructure(structure);
	}
}
