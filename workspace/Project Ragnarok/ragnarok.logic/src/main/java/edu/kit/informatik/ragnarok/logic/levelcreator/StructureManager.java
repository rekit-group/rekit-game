package edu.kit.informatik.ragnarok.logic.levelcreator;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import edu.kit.informatik.ragnarok.logic.levelcreator.parser.FileParser;

public class StructureManager {

	private Map<String, Boolean> settings;

	private Map<Integer, Structure> structures;

	private Random rand;

	private boolean buildInitialFloor;

	/**
	 * Private constructor to prevent instantiation from outside.
	 */
	private StructureManager(int randomSeed) {
		this.settings = new HashMap<String, Boolean>();
		this.structures = new HashMap<Integer, Structure>();
		this.rand = new Random(randomSeed);
	}

	public void init() {
		this.buildInitialFloor = true;
	}

	public static StructureManager load(int randomSeed, String levelName) {
		InputStream levelStream = StructureManager.class.getResourceAsStream("/level_" + levelName + ".dat");

		Scanner scanner = new java.util.Scanner(levelStream);
		scanner.useDelimiter("\\A");
		String input = scanner.hasNext() ? scanner.next() : "";
		scanner.close();

		StructureManager instance = new StructureManager(randomSeed);
		new FileParser(instance, input);
		return instance;
	}

	public Structure next() {
		if (this.buildInitialFloor) {
			this.buildInitialFloor = false;
			return new Structure(new int[][] { { 1, 1, 1, 1, 1, 1, 1, 1 } });
		} else {
			int randId = this.rand.nextInt(this.structures.size());
			int gapWidth = this.rand.nextInt(2) + 1;

			Structure selected = this.structures.get(randId);
			selected.setGap(gapWidth);

			return selected;
		}
	}

	public void setSetting(String settingName, boolean settingValue) {
		this.settings.put(settingName, settingValue);
	}

	public void addStructure(int[][] structureArray) {
		this.structures.put(this.structures.size(), new Structure(structureArray));
	}

}
