package edu.kit.informatik.ragnarok.logic.levelcreator;

import java.util.HashMap;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.bosses.RektSmasher;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class InfiniteLevelCreator extends LevelCreator {

	public InfiniteLevelCreator(int randomSeed) {
		super(randomSeed);

		this.bossRooms = new HashMap<Integer, BossRoom>();

		// first boss room after 200 units in x direction
		this.bossRooms.put(200, new BossRoom(new RektSmasher(new Vec2D()), new LevelStructure(new int[][] {
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
				{ 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
				{ 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
				{ 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1 } }), this));
	}

	private final int[][][] structures = new int[][][] { new int[][] { { 1 } },
	/*
	 * new int[][] { { 1 }, { 5 }, { 1 }, { 1 } }, new int[][] { { 4 }, { 0 }, {
	 * 1 } },
	 * 
	 * new int[][] { { 4 }, { 0 }, { 0 }, { 1 } }, new int[][] { { 4 }, { 0 }, {
	 * 0 }, { 0 }, { 1 } },
	 * 
	 * new int[][] { { 3 }, { 1 } }, new int[][] { { 3 }, { 0 }, { 1 } }, new
	 * int[][] { { 3 }, { 0 }, { 0 }, { 1 } }, new int[][] { { 3 }, { 0 }, { 0
	 * }, { 0 }, { 1 } }, new int[][] { { 3 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 }
	 * }, new int[][] { { 3 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 1 } },
	 * 
	 * new int[][] { { 1 }, { 1 }, }, new int[][] { { 1 }, { 1 }, { 1 } }, new
	 * int[][] { { 1, 0, 0, 1 } }, new int[][] { { 4, 1, 0 }, { 1, 1, 1 }, { 1,
	 * 1, 1 }, }, new int[][] { { 1, 0, 4 }, { 1, 1, 1 }, { 1, 1, 1 } }, new
	 * int[][] { { 0, 2, 1 }, { 1, 1, 1 }, { 1, 1, 1 } }, new int[][] { { 1, 0,
	 * 1 }, { 1, 0, 1 }, { 1, 0, 1 } }, new int[][] { { 0, 0, 0, 1 }, { 0, 0, 1,
	 * 1 }, { 0, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 } }, new int[][] { { 1,
	 * 1 }, { 0, 0 }, { 1, 1 } }, new int[][] { { 0, 2 }, { 1, 1 }, { 0, 0 }, {
	 * 0, 0 }, { 1, 1 } }, new int[][] { { 1, 1, 1 }, { 0, 0, 1 }, { 0, 0, 1 }
	 * }, new int[][] { { 0, 0, 0, 0, 2, 0, 0 }, { 0, 2, 0, 0, 1, 1, 1 }, { 1,
	 * 1, 1, 0, 0, 1, 0 }, { 0, 1, 0, 0, 0, 1, 0 }, { 1, 1, 0, 0, 0, 1, 1 } },
	 * new int[][] { { 0, 2, 0, 0, 1, 1 }, { 1, 1, 0, 0, 1, 1 }, { 1, 1, 0, 0,
	 * 1, 1 }, { 1, 1, 0, 0, 1, 1 } }, new int[][] { { 0, 0, 0, 2, 1 }, { 0, 2,
	 * 1, 0, 1 }, { 1, 0, 1, 0, 1 }, { 1, 0, 1, 0, 1 } }, new int[][] { { 0, 0,
	 * 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 2, 1, 0, 1 }, { 0,
	 * 2, 1, 0, 1, 0, 1 }, { 1, 0, 1, 0, 1, 0, 1 } }, new int[][] { { 0, 0, 0,
	 * 0, 0, 0, 1 }, { 0, 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0, 2, 1 }, { 0, 0,
	 * 1, 0, 1, 0, 1 }, { 0, 0, 1, 0, 1, 0, 0 }, { 1, 0, 1, 0, 1, 0, 0 }, { 1,
	 * 0, 1, 0, 1, 1, 1 } }, new int[][] { { 0, 0, 0, 0, 5 }, { 0, 2, 1, 1, 1 },
	 * { 0, 0, 0, 1, 0 }, { 1, 0, 0, 1, 0 }, { 1, 0, 0, 1, 0 }, { 1, 0, 0, 1, 1
	 * } }, new int[][] { { 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0
	 * }, { 0, 0, 0, 0, 1 }, { 0, 0, 1, 0, 1 }, { 1, 0, 1, 0, 1 }, { 1, 0, 1, 0,
	 * 1 } }, new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1 }, { 0, 0, 0, 0, 0,
	 * 5, 0, 2, 0, 0 }, { 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 }, { 0, 0, 0, 2, 0, 0, 0,
	 * 0, 0, 0 }, { 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 20, 0, 0,
	 * 2, 0 }, { 0, 0, 0, 0, 0, 1, 0, 0, 0, 1 } }, new int[][] { { 0, 0, 0, 0,
	 * 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0,
	 * 0, 0, 0, 0 }, { 1, 2, 0, 1, 0, 2, 1 }, { 1, 0, 0, 0, 0, 0, 1 }, { 1, 1,
	 * 1, 1, 1, 1, 1 } }, new int[][] { { 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0, 1
	 * }, { 0, 0, 0, 0, 0, 1 }, { 0, 1, 0, 2, 0, 1 }, { 0, 1, 0, 0, 0, 1 }, { 1,
	 * 1, 0, 0, 0, 0 }, { 1, 1, 1, 1, 1, 1 } }, new int[][] { { 0, 0, 0, 1, 0,
	 * 0, 0 }, { 0, 0, 1, 0, 1, 0, 0 }, { 0, 1, 0, 0, 0, 1, 5 }, { 1, 0, 0, 2,
	 * 0, 0, 1 }, { 1, 1, 1, 1, 1, 1, 1 } }, new int[][] { { 0, 1, 0, 2, 0, 0,
	 * 0, 1 }, { 0, 1, 0, 0, 2, 0, 0, 1 }, { 1, 1, 0, 0, 0, 0, 1, 1 }, { 1, 1,
	 * 0, 0, 0, 0, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1, 1 } }, new int[][] { { 0, 1 },
	 * { 4, 1 }, { 1, 1 }, { 1, 1 }, { 1, 1 } }, new int[][] { { 0, 1 }, { 0, 1
	 * }, { 0, 4 }, { 0, 0 }, { 1, 1 } }, new int[][] { { 0, 0, 0, 0, 0, 0, 0,
	 * 0, 3 }, { 0, 0, 0, 0, 0, 1, 1, 0, 0 }, { 0, 1, 1, 0, 0, 0, 0, 0, 0 }, {
	 * 0, 0, 0, 2, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0, 0, 0 } }, new int[][]
	 * { { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 1, 1, 1 }, { 0, 0, 1, 0,
	 * 0, 0, 1, 0 }, { 0, 1, 1, 1, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } },
	 * new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1, 1 }, {
	 * 0, 0, 0, 0, 0, 1, 1, 1 }, { 0, 0, 0, 5, 0, 0, 0, 0 }, { 0, 0, 1, 1, 0, 0,
	 * 0, 0 }, { 0, 0, 1, 1, 1, 0, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0, 0 }, }, new
	 * int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0,
	 * 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0,
	 * 1, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0,
	 * 0, 0, 0 }, }
	 */};

	private HashMap<Integer, BossRoom> bossRooms;

	@Override
	public void generate(int max) {

		while (this.generatedUntil < max) {
			// Save current x to where level was generated yet
			int lastGeneratedUntil = this.generatedUntil;

			// Randomly select structure
			int randId = this.rand.nextInt(this.structures.length);

			// Randomly determine gap between this structure and the next
			int gap = this.rand.nextInt(2) + 1;

			// Get structure and all required info
			LevelStructure struc = new LevelStructure(this.structures[randId]);

			// Calculate where to generate structure
			int ix = this.generatedUntil + 1;
			int iy = GameConf.GRID_H;

			// build structure
			struc.buildStructure(this, ix, iy);

			// build gap after structure
			int aw = struc.getWidth();
			this.generateEvenFloor(this.generatedUntil + 1 + aw, this.generatedUntil + aw + gap);

			this.generatedUntil += aw;

			for (int i = lastGeneratedUntil; i <= this.generatedUntil; i++) {
				if (this.bossRooms.containsKey(i)) {
					this.bossRooms.get(i).generate(this.generatedUntil);
				}
			}
		}
	}

}
