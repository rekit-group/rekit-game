package edu.kit.informatik.ragnarok.logic;

import java.util.Random;

import org.eclipse.swt.graphics.RGB;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.EnemyFactory;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateBox;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateFloor;

public class LevelCreator {

	private GameModel model;
	
	public LevelCreator(GameModel model) {
		this.model = model;
		
		generateFloor((int)-c.playerDist, (int)(2 * c.playerDist));
		this.generatedUntil = (int)(2 * c.playerDist);
	}
	
	private int generatedUntil;
	
	
	private final int[][][] structures = new int[][][]{
			new int[][] {
					{3},
			},
			new int[][] {
					{3},
					{0}
			},
			new int[][] {
					{3},
					{0},
					{0}
			},
			new int[][] {
					{3},
					{0},
					{0},
					{0}
			},
			new int[][] {
					{3},
					{0},
					{0},
					{0},
					{0}
			},
			new int[][] {
					{3},
					{0},
					{0},
					{0},
					{0},
					{0}
			},
			new int[][] {
					{1},
			},
			new int[][] {
					{1},
					{1},
			},
			new int[][] {
					{0, 1, 0},
					{1, 1, 1}
			},
			new int[][] {
					{1, 0, 0},
					{1, 1, 1}
			},
			new int[][] {
					{0, 2, 1},
					{1, 1, 1}
			},
			new int[][] {
					{1, 0, 1},
					{1, 0, 1}
			},
			new int[][] {
					{0, 0, 0, 1},
					{0, 0, 1, 1},
					{0, 1, 1, 1},
					{1, 1, 1, 1}
			},
			new int[][] {
					{1, 1},
					{2, 0}
			},
			new int[][] {
					{0, 2},
					{1, 1},
					{0, 0},
					{0, 0}
			},
			new int[][] {
					{1, 1, 1},
					{0, 0, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 2, 0, 0},
					{0, 2, 0, 0, 1, 1, 1},
					{1, 1, 1, 0, 0, 1, 0},
					{0, 1, 0, 0, 0, 1, 0}
			},
			new int[][] {
					{0, 2, 0, 0, 1, 1},
					{1, 1, 0, 0, 1, 1},
					{1, 1, 0, 0, 1, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 1},
					{0, 0, 1, 0, 1},
					{1, 0, 1, 0, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 0, 0, 1},
					{0, 0, 0, 0, 0, 0, 1},
					{0, 0, 0, 0, 1, 0, 1},
					{0, 0, 1, 0, 1, 0, 1},
					{1, 0, 1, 0, 1, 0, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 0, 0, 1},
					{0, 0, 0, 0, 0, 0, 1},
					{0, 0, 0, 0, 0, 0, 1},
					{0, 0, 1, 0, 1, 0, 1},
					{0, 0, 1, 0, 1, 0, 0},
					{1, 0, 1, 0, 1, 0, 0}
			},
			new int[][] {
					{0, 0, 0, 0, 0},
					{0, 2, 1, 1, 1},
					{0, 0, 0, 1, 0},
					{1, 0, 0, 1, 0},
					{1, 0, 0, 1, 0}
			},
			new int[][] {
					{0, 0, 0, 0, 1},
					{0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0},
					{0, 0, 0, 0, 1},
					{0, 0, 1, 0, 1},
					{1, 0, 1, 0, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
					{0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
					{0, 0, 0, 0, 1, 1, 0, 2, 0, 1},
					{0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
					{1, 1, 0, 2, 0, 0, 0, 0, 0, 1},
					{1, 0, 0, 0, 0, 0, 0, 0, 0, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0},
					{1, 0, 2, 1, 0, 2, 1},
					{1, 0, 2, 0, 0, 2, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 0, 1},
					{0, 0, 0, 0, 0, 1},
					{0, 0, 0, 0, 0, 1},
					{0, 1, 0, 2, 0, 1},
					{0, 1, 0, 0, 0, 1},
					{1, 1, 0, 0, 0, 0}
			},
			new int[][] {
					{0, 0, 0, 1, 0, 0, 0},
					{0, 0, 1, 0, 1, 0, 0},
					{0, 1, 0, 2, 0, 1, 0},
					{1, 0, 0, 2, 0, 0, 1}
			},
			new int[][] {
					{0, 1, 0, 2, 0, 0, 0, 1},
					{0, 1, 0, 0, 2, 0, 0, 1},
					{1, 1, 0, 0, 0, 0, 1, 1},
					{1, 1, 0, 0, 0, 0, 1, 1}
			},
			new int[][] {
					{0, 1},
					{0, 1},
					{1, 1},
					{1, 1}
			},
			new int[][] {
					{0, 1},
					{0, 1},
					{0, 0},
					{0, 0}
			},
	};
	
	public void generateFloor(int fromX, int toX) {
		Inanimate i;
		Random r = new Random();
		
		// build floor under this structure and under the gap
		for (int x = fromX; x <= toX; x++) {
			if (r.nextInt(8) != 0 || true) {
				int randColG = r.nextInt(100) + 100;
				int randColRB = r.nextInt(40) + 30;
				i = new InanimateFloor(new Vec2D(x, c.gridH - 1), new Vec2D(1, 1), new RGB(randColRB, randColG, randColRB));
				this.model.addGameElement(i);
			}
		}
	}
	
	public void generate() {
		int max = (int) this.model.getCurrentOffset() + c.gridW + 1;
	
		while (this.generatedUntil < max) {
			
			// Randomly select structure
			Random r = new Random();
			int randId = r.nextInt(structures.length);
			
			// Randomly determine gap between this structure and the next
			int gap = r.nextInt(3) + 2;
			
			// Get structure and all required info
			int[][] struc = this.structures[randId];
			int ah = struc.length;
			int aw = struc[0].length;
			
			int ix = generatedUntil + 1;
			int iy = c.gridH - 1;
			
			generateFloor(ix, ix + aw + gap);
			
			Inanimate i;
			
			// build structure
			for (int y = 0; y < ah; y++) {
				for (int x = 0; x < aw; x++) {
					if (struc[y][x] == 1) {
						int randCol = r.nextInt(60) + 50;
						i = new InanimateBox(new Vec2D(ix + x, iy + (y - ah)), new Vec2D(1, 1), new RGB(randCol, randCol, randCol));
						this.model.addGameElement(i);
					}
					else if (struc[y][x] > 1) {
						EnemyFactory.generate(struc[y][x], ix + x, iy + (y - ah));
					}
					
				}
			}	
			
			// Update coordinate until where we generated
			this.generatedUntil += aw + gap;
		}
	}
	
}
