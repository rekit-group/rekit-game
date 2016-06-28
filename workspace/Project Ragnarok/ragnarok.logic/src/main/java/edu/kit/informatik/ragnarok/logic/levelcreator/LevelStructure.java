package edu.kit.informatik.ragnarok.logic.levelcreator;

import java.util.Random;

import edu.kit.informatik.ragnarok.logic.gameelements.entities.EntityFactory;

public class LevelStructure {

	private final int[][] structureArray;

	public LevelStructure(int[][] structureArray) {
		this.structureArray = structureArray;
		for (int i = 1; i < structureArray.length; i++) {
			if (structureArray[i].length != this.getWidth()) {
				throw new IllegalArgumentException("LevelStructure array must be rectangular!");
			}
		}
	}

	public int getWidth() {
		return this.structureArray[0].length;
	}

	public int getHeight() {
		return this.structureArray.length;
	}

	public void buildStructure(LevelCreator levelCreator, int ix, int iy) {
		Random r = new Random();
		// save structures dimension
		int ah = this.getHeight();
		int aw = this.getWidth();
		// build structure
		for (int y = 0; y < ah; y++) {
			for (int x = 0; x < aw; x++) {
				if (this.structureArray[y][x] == 1) {
					if (y == ah - 1) {
						levelCreator.generateFloor(ix + x, iy + (y - ah));
					} else {
						levelCreator.generateBox(ix + x, iy + (y - ah));
					}
				} else if (this.structureArray[y][x] > 1) {
					EntityFactory.generate(this.structureArray[y][x], ix + x, iy + (y - ah));
				} else if (this.structureArray[y][x] == 0) {
					if (r.nextInt(10) == 0) {
						EntityFactory.generate(10, ix + x, iy + (y - ah));
					}
				}

			}
		}
	}

}
