package edu.kit.informatik.ragnarok.logic;

import java.util.Random;

import org.eclipse.swt.graphics.RGB;

import edu.kit.infomatik.config.c;
import edu.kit.informatik.ragnarok.logic.gameelements.Inanimate;

public class LevelCreator {

	private GameModel model;
	
	public LevelCreator(GameModel model) {
		this.model = model;
	}
	
	private int generatedUntil;
	
	
	private final int[][][] structures = new int[][][]{
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
					{0, 0, 1},
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
					{0, 0}
			},
			new int[][] {
					{1, 1},
					{0, 0},
					{0, 0}
			},
			new int[][] {
					{1, 1, 1},
					{0, 0, 1}
			},
			new int[][] {
					{0, 0, 0, 0, 1, 1, 1},
					{1, 1, 1, 0, 0, 1, 0},
					{0, 1, 0, 0, 0, 1, 0}
			},
			new int[][] {
					{0, 0, 0, 0, 1, 1},
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
					{0, 0, 1, 1, 1},
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
			}
			/*
			new int[][] {
					{0, 0, 0, 0, 1},
			},
			new int[][] {
					{0, 0, 0, 0, 1},
					{0, 0, 0, 0, 1},
			}
			*/
	};
	
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
			
			Inanimate i;
			
			// build floor under this structure and under the gap
			for (int x = 0; x < aw + gap; x++) {
				int randCol = r.nextInt(60) + 50;
				i = new Inanimate(new Vec2D(ix + x, iy), new Vec2D(1, 1), new RGB(randCol, randCol, randCol));
				this.model.addGameElement(i);
			}
			
			// build structure
			for (int y = 0; y < ah; y++) {
				for (int x = 0; x < aw; x++) {
					if (struc[y][x] == 1) {
						int randCol = r.nextInt(60) + 50;
						i = new Inanimate(new Vec2D(ix + x, iy + (y - ah)), new Vec2D(1, 1), new RGB(randCol, randCol, randCol));
						this.model.addGameElement(i);
					}
					
				}
			}	
			
			// Update coordinate until where we generated
			this.generatedUntil += aw + gap;
		}
	}
	
}
