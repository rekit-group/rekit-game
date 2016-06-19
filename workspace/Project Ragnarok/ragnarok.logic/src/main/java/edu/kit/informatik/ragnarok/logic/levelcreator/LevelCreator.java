package edu.kit.informatik.ragnarok.logic.levelcreator;

import java.util.Random;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.gameelements.GameElement;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.Inanimate;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateBox;
import edu.kit.informatik.ragnarok.logic.gameelements.inanimate.InanimateFloor;
import edu.kit.informatik.ragnarok.primitives.Vec2D;
import edu.kit.informatik.ragnarok.util.RGBColor;

public abstract class LevelCreator {

	private GameModel model;

	public LevelCreator(GameModel model) {
		// save reference to GameMode to be able to add GameElements
		this.model = model;

		// build initial even floor
		for (int x = (int) -GameConf.playerDist; x <= (int) (2 * GameConf.playerDist); x++) {
			this.generateFloor(x, GameConf.gridH);
		}
		this.generatedUntil = (int) (2 * GameConf.playerDist);
	}

	protected int generatedUntil;

	public void generate(int max) {
		// must be implemented
	}

	protected void generateGameElement(GameElement element) {
		this.model.addGameElement(element);
	}

	protected void generateFloor(int x, int y) {
		Inanimate i;
		Random r = new Random();

		int randColG = r.nextInt(100) + 100;
		int randColRB = r.nextInt(40) + 30;
		i = new InanimateFloor(new Vec2D(x, GameConf.gridH - 1), new Vec2D(1, 1), new RGBColor(randColRB, randColG, randColRB));
		this.generateGameElement(i);
	}

	protected void generateBox(int x, int y) {
		Inanimate i;
		Random r = new Random();
		int randCol = r.nextInt(60) + 50;
		i = new InanimateBox(new Vec2D(x, y), new Vec2D(1, 1), new RGBColor(randCol, randCol, randCol));
		this.generateGameElement(i);
	}
}
