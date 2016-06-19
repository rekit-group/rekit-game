package edu.kit.informatik.ragnarok.logic.levelcreator;

import java.util.Random;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;

public class InfiniteLevelCreator extends LevelCreator {
	
	public InfiniteLevelCreator(GameModel model) {
		super(model);
	}

	private final int[][][] structures = new int[][][]{
			new int[][] {
					{1},
					{5},
					{1},
					{1}
			},
			new int[][] {
					{4},
					{0},
					{1}
			},

			new int[][] {
					{4},
					{0},
					{0},
					{1}
			},
			new int[][] {
					{4},
					{0},
					{0},
					{0},
					{1}
			},
			
			new int[][] {
					{3},
					{1}
			},
			new int[][] {
					{3},
					{0},
					{1}
			},
			new int[][] {
					{3},
					{0},
					{0},
					{1}
			},
			new int[][] {
					{3},
					{0},
					{0},
					{0},
					{1}
			},
			new int[][] {
					{3},
					{0},
					{0},
					{0},
					{0},
					{1}
			},
			new int[][] {
					{3},
					{0},
					{0},
					{0},
					{0},
					{0},
					{1}
			},
			
			new int[][] {
					{1},
					{1},
			},
			new int[][] {
					{1},
					{1},
					{1}
			},
			new int[][] {
				{1, 0, 0, 1}
			},
			new int[][] {
					{4, 1, 0},
					{1, 1, 1},
					{1, 1, 1},
			},
			new int[][] {
					{1, 0, 4},
					{1, 1, 1},
					{1, 1, 1}
			},
			new int[][] {
					{0, 2, 1},
					{1, 1, 1},
					{1, 1, 1}
			},
			new int[][] {
					{1, 0, 1},
					{1, 0, 1},
					{1, 0, 1}
			},
			new int[][] {
					{0, 0, 0, 1},
					{0, 0, 1, 1},
					{0, 1, 1, 1},
					{1, 1, 1, 1},
					{1, 1, 1, 1}
			},
			new int[][] {
					{1, 1},
					{0, 0},
					{1, 1}
			},
			new int[][] {
					{0, 2},
					{1, 1},
					{0, 0},
					{0, 0},
					{1, 1}
			},
			new int[][] {
					{1, 1, 1},
					{0, 0, 1},
					{0, 0, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 2, 0, 0},
					{0, 2, 0, 0, 1, 1, 1},
					{1, 1, 1, 0, 0, 1, 0},
					{0, 1, 0, 0, 0, 1, 0},
					{1, 1, 0, 0, 0, 1, 1}
			},
			new int[][] {
					{0, 2, 0, 0, 1, 1},
					{1, 1, 0, 0, 1, 1},
					{1, 1, 0, 0, 1, 1},
					{1, 1, 0, 0, 1, 1}
			},
			new int[][] {
					{0, 0, 0, 2, 1},
					{0, 2, 1, 0, 1},
					{1, 0, 1, 0, 1},
					{1, 0, 1, 0, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 0, 0, 1},
					{0, 0, 0, 0, 0, 0, 1},
					{0, 0, 0, 2, 1, 0, 1},
					{0, 2, 1, 0, 1, 0, 1},
					{1, 0, 1, 0, 1, 0, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 0, 0, 1},
					{0, 0, 0, 0, 0, 0, 1},
					{0, 0, 0, 0, 0, 2, 1},
					{0, 0, 1, 0, 1, 0, 1},
					{0, 0, 1, 0, 1, 0, 0},
					{1, 0, 1, 0, 1, 0, 0},
					{1, 0, 1, 0, 1, 1, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 5},
					{0, 2, 1, 1, 1},
					{0, 0, 0, 1, 0},
					{1, 0, 0, 1, 0},
					{1, 0, 0, 1, 0},
					{1, 0, 0, 1, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 1},
					{0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0},
					{0, 0, 0, 0, 1},
					{0, 0, 1, 0, 1},
					{1, 0, 1, 0, 1},
					{1, 0, 1, 0, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
					{0, 0, 0, 0, 0, 5, 0, 2, 0, 0},
					{0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
					{0, 0, 0, 2, 0, 0, 0, 0, 0, 0},
					{1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 20, 0, 0, 2, 0},
					{0, 0, 0, 0, 0, 1, 0, 0, 0, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0},
					{1, 2, 0, 1, 0, 2, 1},
					{1, 0, 0, 0, 0, 0, 1},
					{1, 1, 1, 1, 1, 1, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 0, 1},
					{0, 0, 0, 0, 0, 1},
					{0, 0, 0, 0, 0, 1},
					{0, 1, 0, 2, 0, 1},
					{0, 1, 0, 0, 0, 1},
					{1, 1, 0, 0, 0, 0},
					{1, 1, 1, 1, 1, 1}
			},
			new int[][] {
					{0, 0, 0, 1, 0, 0, 0},
					{0, 0, 1, 0, 1, 0, 0},
					{0, 1, 0, 0, 0, 1, 5},
					{1, 0, 0, 2, 0, 0, 1},
					{1, 1, 1, 1, 1, 1, 1}
			},
			new int[][] {
					{0, 1, 0, 2, 0, 0, 0, 1},
					{0, 1, 0, 0, 2, 0, 0, 1},
					{1, 1, 0, 0, 0, 0, 1, 1},
					{1, 1, 0, 0, 0, 0, 1, 1},
					{1, 1, 1, 1, 1, 1, 1, 1}
			},
			new int[][] {
					{0, 1},
					{4, 1},
					{1, 1},
					{1, 1},
					{1, 1}
			},
			new int[][] {
					{0, 1},
					{0, 1},
					{0, 4},
					{0, 0},
					{1, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 0, 0, 0, 0, 3},
					{0, 0, 0, 0, 0, 1, 1, 0, 0},
					{0, 1, 1, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 2, 0, 0, 0, 0, 0},
					{1, 0, 0, 0, 0, 0, 0, 0, 0}
			},
			new int[][] {
					{0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 1, 1, 1},
					{0, 0, 1, 0, 0, 0, 1, 0},
					{0, 1, 1, 1, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0, 0}
			},
			new int[][] {
					{0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 1, 1},
					{0, 0, 0, 0, 0, 1, 1, 1},
					{0, 0, 0, 5, 0, 0, 0, 0},
					{0, 0, 1, 1, 0, 0, 0, 0},
					{0, 0, 1, 1, 1, 0, 0, 0},
					{1, 0, 0, 0, 0, 0, 0, 0},
			},
			new int[][] {
					{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
					{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			}
	};
	
	public void generate(int max) {
	
		while (this.generatedUntil < max) {
			
			// Randomly select structure
			Random r = new Random();
			int randId = r.nextInt(structures.length);
			
			// Randomly determine gap between this structure and the next
			int gap = r.nextInt(2) + 1;
			
			// Get structure and all required info
			LevelStructure struc = new LevelStructure(this.structures[randId]);
			
			// Calculate where to generate structure
			int ix = generatedUntil + 1;
			int iy = GameConf.gridH;
			
			// build structure
			struc.buildStructure(this, ix, iy);
			
			// build gap after structure
			int aw = struc.getWidth();
			for (int x = generatedUntil + 1 + aw; x <=  generatedUntil + aw + gap; x++) {
				generateFloor(x, GameConf.gridH);
			}

			// Update coordinate until where we generated
			this.generatedUntil += aw + gap;
		}
	}
	
}
