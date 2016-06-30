package edu.kit.informatik.ragnarok.logic.levelcreator;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import edu.kit.informatik.ragnarok.logic.gameelements.GameElementFactory;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.type.Boss;
import edu.kit.informatik.ragnarok.logic.levelcreator.bossstructure.BossStructure;
import edu.kit.informatik.ragnarok.logic.levelcreator.parser.FileParser;
import edu.kit.informatik.ragnarok.primitives.Vec;

public class StructureManager extends Configurable {

	private Map<Integer, Structure> structures;

	private Random rand;

	private int currentStructureId = -1;

	public BossSettings bossSettings;

	private int unitsBuilt = 0;

	private int lastUnitsBuilt = 0;

	/**
	 * Private constructor to prevent instantiation from outside.
	 */
	private StructureManager(int randomSeed) {
		super();
		this.structures = new HashMap<Integer, Structure>();
		this.rand = new Random(randomSeed);
	}

	public void init() {
		this.currentStructureId = -1;
		this.unitsBuilt = 0;
	}

	public static StructureManager load(int randomSeed, String levelName) {
		InputStream levelStream = StructureManager.class.getResourceAsStream("/level_" + levelName + ".dat");

		Scanner scanner = new java.util.Scanner(levelStream);
		scanner.useDelimiter("\\A");
		String input = scanner.hasNext() ? scanner.next() : "";
		scanner.close();

		StructureManager instance = new StructureManager(randomSeed);

		// WTF is this syntax
		// Java, I don't like you anymore
		// Usually you are so consequent and intuitive
		// But what is this?????
		instance.bossSettings = instance.new BossSettings();

		new FileParser(instance, input);
		return instance;
	}

	public Structure next() {

		// if this is level start: build initial Structure
		if (this.unitsBuilt == 0) {
			return this.getInitialStructure();
		} else {
			// increment structureId / count
			this.currentStructureId++;

			// if end of sequence reached: Determine if to repeat or end level
			if (this.currentStructureId >= this.structures.size()) {
				if (this.isSettingSet("infinite")) {
					this.currentStructureId = 0;
				} else {
					return this.getEndStructure();
				}
			}

			Structure bossStructureOrNull = this.bossSettings.getNextOrNull(this.lastUnitsBuilt, this.unitsBuilt);
			this.lastUnitsBuilt = this.unitsBuilt;

			if (bossStructureOrNull != null) {
				return bossStructureOrNull;
			}

			// get next Structure depending on strategy (shuffled / in order)
			if (this.isSettingSet("shuffle")) {
				return this.nextShuffeled();
			} else {
				return this.nextInOrder();
			}
		}
	}

	public Structure nextShuffeled() {
		// get random next Structure
		int randId = this.rand.nextInt(this.structures.size());
		Structure selected = this.structures.get(randId);

		// determine and set gap width
		int gap = this.nextGapWidth();
		selected.setGap(gap);

		// keep track of how far we built
		this.unitsBuilt += selected.getWidth() + gap;

		return selected;
	}

	public Structure nextInOrder() {
		// get next Structure in order
		Structure selected = this.structures.get(this.currentStructureId);

		// determine and set gap width
		int gap = this.nextGapWidth();
		selected.setGap(gap);

		// keep track of how far we built
		this.unitsBuilt += selected.getWidth() + gap;

		return selected;
	}

	public int nextGapWidth() {
		return this.isSettingSet("doGaps") ? this.rand.nextInt(2) + 1 : 0;
	}

	public void addStructure(Structure structure) {
		this.structures.put(this.structures.size(), structure);
	}

	public class BossSettings extends Configurable {
		public Structure getNextOrNull(int fromX, int toX) {
			for (int i = fromX; i <= toX; i++) {
				String numString = String.valueOf(i);
				if (this.isSettingSet(numString)) {
					int bossId = this.getSettingValue(numString);
					Structure bossStructure = new BossStructure((Boss) GameElementFactory.getPrototype(bossId).create(new Vec()));
					StructureManager.this.unitsBuilt += bossStructure.getWidth();
					return bossStructure;
				}
			}
			return null;
		}
	}

	private Structure getInitialStructure() {
		// Flat floor
		Structure structure = new Structure(new int[][] { { 1, 1, 1, 1, 1, 1, 1, 1 } });

		// keep track of how far we built
		this.unitsBuilt += structure.getWidth();

		return structure;
	}

	private Structure getEndStructure() {
		// Vertical wall
		Structure structure = new Structure(new int[][] { { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 }, { 1 } });

		// keep track of how far we built
		this.unitsBuilt += structure.getWidth();

		return structure;
	}

}
