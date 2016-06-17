package edu.kit.informatik.ragnarok.logic;

import java.util.Random;

import org.eclipse.swt.graphics.RGB;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.enemies.EnemyFactory;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateBox;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateFloor;
import edu.kit.informatik.ragnarok.primitives.Vec2D;

public class LevelCreator {

	private GameModel model;
	
	public LevelCreator(GameModel model) {
		this.model = model;
		
		for (int x = (int)-GameConf.playerDist; x <= (int)(2 * GameConf.playerDist); x++) {
			generateFloor(x, GameConf.gridH);
		}
		this.generatedUntil = (int)(2 * GameConf.playerDist);
	}
	
	private int generatedUntil;
	
	
	private final int[][][] structures = new int[][][]{
			new int[][] {
					{5},
					{5},
					{5},
					{5},
					{5},
					{5},
					{5},
					{1}
			},
			new int[][] {
					{1},
					{5},
					{1},
					{1}
			},
			new int[][] {
					{1},
					{5},
					{1},
					{1}
			},
			new int[][] {
					{1},
					{5},
					{1},
					{1}
			},
			new int[][] {
					{1},
					{5},
					{1},
					{1}
			},
			new int[][] {
					{1},
					{5},
					{1},
					{1}
			},
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
					{0, 0, 0, 0, 0},
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
					{0, 0, 0, 0, 0, 0, 0, 2, 0, 0},
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
					{0, 1, 0, 0, 0, 1, 0},
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
					{0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 1, 1, 0, 0, 0, 0},
					{0, 0, 1, 1, 1, 0, 0, 0},
					{1, 0, 0, 0, 0, 0, 0, 0},
			},
	};
	
	public void generateFloor(int x, int y) {
		Inanimate i;
		Random r = new Random();
		
		int randColG = r.nextInt(100) + 100;
		int randColRB = r.nextInt(40) + 30;
		i = new InanimateFloor(new Vec2D(x, GameConf.gridH - 1), new Vec2D(1, 1), new RGB(randColRB, randColG, randColRB));
		this.model.addGameElement(i);
	}
	public void generateBox(int x, int y) {
		Inanimate i;
		Random r = new Random();
		int randCol = r.nextInt(60) + 50;
		i = new InanimateBox(new Vec2D(x, y), new Vec2D(1, 1), new RGB(randCol, randCol, randCol));
		this.model.addGameElement(i);
	}
	
	public void generate() {
		int max = (int) this.model.getCurrentOffset() + GameConf.gridW + 1;
	
		while (this.generatedUntil < max) {
			
			// Randomly select structure
			Random r = new Random();
			int randId = r.nextInt(structures.length);
			
			// Randomly determine gap between this structure and the next
			int gap = r.nextInt(2) + 1;
			
			// Get structure and all required info
			int[][] struc = this.structures[randId];
			int ah = struc.length;
			int aw = struc[0].length;
			
			int ix = generatedUntil + 1;
			int iy = GameConf.gridH;
			
			// build structure
			for (int y = 0; y < ah; y++) {
				for (int x = 0; x < aw; x++) {
					if (struc[y][x] == 1) {
						if (y == ah - 1) {
							generateFloor(ix + x, iy + (y - ah));	
						}
						else {
							generateBox(ix + x, iy + (y - ah));	
						}
					}
					else if (struc[y][x] > 1) {
						EnemyFactory.generate(struc[y][x], ix + x, iy + (y - ah));
					}
					else if (struc[y][x] == 0) {
						if (r.nextInt(10) == 0) {
							EnemyFactory.generate(10, ix + x, iy + (y - ah));
						}
					}
					
				}
			}	
			
			for (int x = generatedUntil + 1 + aw; x <=  generatedUntil + aw + gap; x++) {
				generateFloor(x, GameConf.gridH);
			}

			// Update coordinate until where we generated
			this.generatedUntil += aw + gap;
		}
	}
	
}
