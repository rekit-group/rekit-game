package ragnarok.logic.gui;

import ragnarok.config.GameConf;
import ragnarok.core.GameGrid;
import ragnarok.logic.IScene;
import ragnarok.logic.gameelements.entities.Player;
import ragnarok.primitives.geometry.Vec;
import ragnarok.util.TextOptions;

/**
 *
 * This {@link GuiElement} realizes a status view of the {@link Player Player's}
 * Score.
 *
 */
public class ScoreGui extends GuiElement {
	/**
	 * The points of the {@link Player}.
	 */
	private Text points;
	/**
	 * The Highscore of the current Level.
	 */
	private Text highscore;
	/**
	 * The text options.
	 */
	private TextOptions op;

	/**
	 * Create the status view.
	 *
	 * @param scene
	 *            the scene
	 */
	public ScoreGui(IScene scene) {
		super(scene);
		this.op = new TextOptions(new Vec(-1, 0), GameConf.GAME_TEXT_SIZE, GameConf.GAME_TEXT_COLOR, GameConf.GAME_TEXT_FONT, 1);
		this.points = new Text(scene, this.op);
		this.highscore = new Text(scene, this.op);
		this.points.setPos(new Vec(GameConf.PIXEL_W - 10, 10));
		this.highscore.setPos(new Vec(GameConf.PIXEL_W - 10, 50));
	}

	@Override
	public void logicLoop() {
		this.points.setText(this.scene.getScore() + " Points");
		this.highscore.setText(this.scene.getHighScore() + " Highscore");
	}

	@Override
	public void internalRender(GameGrid f) {
		this.points.internalRender(f);
		this.highscore.internalRender(f);
	}

}
