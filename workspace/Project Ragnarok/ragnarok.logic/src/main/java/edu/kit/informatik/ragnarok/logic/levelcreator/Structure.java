package edu.kit.informatik.ragnarok.logic.levelcreator;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElementFactory;

public class Structure {

	private int[][] structureArray;

	private int gapWidth;

	public Structure(int[][] structureArray) {
		this.structureArray = structureArray;
	}

	public int build(int levelX) {
		// build structure
		for (int y = 0; y < this.structureArray.length; y++) {
			for (int x = 0; x < this.structureArray[0].length; x++) {
				int aY = (GameConf.GRID_H - this.structureArray.length) + y;
				if (this.structureArray[y][x] > 0) {
					GameElementFactory.generate(this.structureArray[y][x], levelX + x, aY);
				}
			}
		}
		// add gap
		for (int x = 0; x < this.gapWidth; x++) {
			GameElementFactory.generate(1, levelX + this.structureArray[0].length + x, GameConf.GRID_H - 1);
		}
		return this.getWidth() + this.gapWidth;
	}

	public int getHeight() {
		return this.structureArray.length;
	}

	public int getWidth() {
		return this.structureArray[0].length;
	}

	public void setGap(int gapWidth) {
		this.gapWidth = gapWidth;
	}

}
