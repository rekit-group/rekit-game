package edu.kit.informatik.ragnarok.logic.levelcreator;

import java.util.Random;

public class LevelAssembler {

	private int generatedUntil;
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

			// build structure
			this.generatedUntil += this.manager.next().build(this.generatedUntil + 1, this.manager.isSettingSet("autoCoinSpawn"));

		}
	}

}
