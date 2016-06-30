package edu.kit.informatik.ragnarok.logic.levelcreator;

import java.util.Random;

public class LevelAssembler {

	private int generatedUntil;
	private int lastGeneratedUntil;

	private StructureManager manager;

	public LevelAssembler(String levelName) {
		this(levelName, new Random().nextInt());
	}

	public LevelAssembler(String levelName, int seed) {
		this.manager = StructureManager.load(seed, levelName);
	}

	public void init() {
		this.generatedUntil = 0;
		this.manager.init();
	}

	public void generate(int max) {

		while (this.generatedUntil < max) {

			Structure nextStructure;

			// check if Manager delivers u a BossStructure
			Structure bossStructureOrNull = this.manager.bossSettings.getNextOrNull(this.lastGeneratedUntil, this.generatedUntil);
			if (bossStructureOrNull != null) {
				// If so: we want it!
				nextStructure = bossStructureOrNull;
			} else {
				// Otherwise randomly select structure
				nextStructure = this.manager.next();
			}

			// Save current x to where level was generated yet
			this.lastGeneratedUntil = this.generatedUntil;

			// build structure
			this.generatedUntil += nextStructure.build(this.generatedUntil + 1, this.manager.isSettingSet("autoCoinSpawn"));

		}
	}

}
