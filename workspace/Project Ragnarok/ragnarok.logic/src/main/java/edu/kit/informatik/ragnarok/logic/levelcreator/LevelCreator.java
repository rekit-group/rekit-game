package edu.kit.informatik.ragnarok.logic.levelcreator;

import java.util.Random;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateBox;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateFloor;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBColor;

public abstract class LevelCreator {
	
	private Scene scene;
	
	protected Random rand;
	
	protected Random colorRand;
	
	public LevelCreator(int randomSeed) {
		// save reference to GameMode to be able to add GameElements
		this.scene = scene;
		
		rand = new Random(randomSeed);
		colorRand = new Random();
	}
	
	public void init(Scene scene) {
		this.scene = scene;
		
		// build initial even floor 
		for (int x = (int)-GameConf.PLAYER_CAMERA_OFFSET; x <= (int)(2 * GameConf.PLAYER_CAMERA_OFFSET); x++) {
			generateFloor(x, GameConf.GRID_H);
		}
		this.generatedUntil = (int)(2 * GameConf.PLAYER_CAMERA_OFFSET);
	}
	
	protected int generatedUntil;
	
	/**
	 * Generates the level up to a maximum unit
	 * @param max the max unit to generate to
	 */
	public abstract void generate(int max);
	
	protected void generateGameElement(GameElement element) {
		this.scene.addGameElement(element);
	}
	
	protected Scene getScene() {
		return this.scene;
	}
	
	protected void generateFloor(int x, int y) {
		Inanimate i;
		
		int randColG = colorRand.nextInt(100) + 100;
		int randColRB = colorRand.nextInt(40) + 30;
		i = new InanimateFloor(new Vec2D(x, GameConf.GRID_H - 1), new Vec2D(1, 1), new RGBColor(randColRB, randColG, randColRB));
		generateGameElement(i);
	}

	public void generateEvenFloor(int fromX, int toX) {
		for (int x = fromX; x <= toX; x++) {
			generateFloor(x, GameConf.GRID_H);
		}

		// Update coordinate until where we generated
		this.generatedUntil += toX - fromX + 1;
	}
	
	protected void generateBox(int x, int y) {
		Inanimate i;
		int randCol = colorRand.nextInt(60) + 50;
		i = new InanimateBox(new Vec2D(x, y), new Vec2D(1, 1), new RGBColor(randCol, randCol, randCol));
		generateGameElement(i);
	}
}
