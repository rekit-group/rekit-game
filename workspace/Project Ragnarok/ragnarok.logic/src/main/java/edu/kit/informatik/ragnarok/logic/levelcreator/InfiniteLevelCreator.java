package edu.kit.informatik.ragnarok.logic.levelcreator;

import java.util.Random;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateDoor;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

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
			
			/*
			generateBossRoom(new LevelStructure(new int[][] {
			  {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			  {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			  {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			  {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			  {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			  {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			  {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			  {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
			}), this.generatedUntil);
			*/
			
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
			generateEvenFloor(generatedUntil + 1 + aw, generatedUntil + aw + gap);
			
			this.generatedUntil += aw;
			
		}
	}
	
	public void generateEvenFloor(int fromX, int toX) {
		for (int x = fromX; x <= toX; x++) {
			generateFloor(x, GameConf.gridH);
		}

		// Update coordinate until where we generated
		this.generatedUntil += toX - fromX;
	}
	
	
	public void generateBossRoom(LevelStructure roomStructure, int x) {
		
		// generate floor before boss room 
		generateEvenFloor(x, x + 5);
		// generate boss room structure
		roomStructure.buildStructure(this, x + 6, GameConf.gridH);
		this.generatedUntil += roomStructure.getWidth();
		// generate floor after boss room
		generateEvenFloor(x + 6 + roomStructure.getWidth(), x + 6 + roomStructure.getWidth() + 5);
		
		// generate door after room
		InanimateDoor door = new InanimateDoor(new Vec2D(x + 5 + roomStructure.getWidth(), (float)Math.ceil(GameConf.gridH / 2)));
		this.generateGameElement(door);
		
	}
	
}
