package edu.kit.informatik.ragnarok.logic.gui;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.Field;
import edu.kit.informatik.ragnarok.core.GuiElement;
import edu.kit.informatik.ragnarok.core.Scene;
import edu.kit.informatik.ragnarok.primitives.geometry.Vec;
import edu.kit.informatik.ragnarok.util.TextOptions;

/**
 *
 * This {@link GuiElement} realizes a status view of the {@link Player Player's}
 * Score
 *
 */
public class ScoreGui extends GuiElement {
	/**
	 * The points of the {@link Player}
	 */
	private Text points;
	/**
	 * The Highscore of the current Level
	 */
	private Text highscore;
	/**
	 * The text options
	 */
	private TextOptions op;

	/**
	 * Create the status view
	 *
	 * @param scene
	 *            the scene
	 */
	public ScoreGui(Scene scene) {
		super(scene);
		this.op = new TextOptions(new Vec(-1, 0), GameConf.GAME_TEXT_SIZE, GameConf.GAME_TEXT_COLOR, GameConf.GAME_TEXT_FONT, 1);
		this.points = new Text(scene, this.op);
		this.highscore = new Text(scene, this.op);
		this.points.setPos(new Vec(GameConf.PIXEL_W - 10, 10));
		this.highscore.setPos(new Vec(GameConf.PIXEL_W - 10, 50));
	}

	@Override
	public void logicLoop(float deltaTime) {
		this.points.setText(this.scene.getScore() + " Points");
		this.highscore.setText(this.scene.getHighScore() + " Highscore");
	}

	@Override
	public void internalRender(Field f) {
		this.points.internalRender(f);
		this.highscore.internalRender(f);
	}

}
