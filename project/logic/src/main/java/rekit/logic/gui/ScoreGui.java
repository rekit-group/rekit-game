package rekit.logic.gui;

import rekit.config.GameConf;
import rekit.core.GameGrid;
import rekit.logic.ILevelScene;
import rekit.logic.gameelements.entities.Player;
import rekit.primitives.TextOptions;
import rekit.primitives.geometry.Vec;

/**
 *
 * This {@link GuiElement} realizes a status view of the {@link Player Player's}
 * Score.
 *
 */
public class ScoreGui extends LevelGuiElement {
	/**
	 * Current score of the {@link Player}.
	 */
	private int score;
	/**
	 * The highscore of the current Level.
	 */
	private int highscore;
	/**
	 * Text that prints the score on the GUI.
	 */
	private Text scoreText;
	/**
	 * Text that prints the highscore on the GUI.
	 */
	private Text highscoreText;
	/**
	 * The text options for this.highscoreText and this.pointsText.
	 */
	private TextOptions op;

	/**
	 * Create the status view.
	 *
	 * @param scene
	 *            the scene
	 */
	public ScoreGui(ILevelScene scene) {
		super(scene);
		this.op = new TextOptions(new Vec(-1, 0), GameConf.GAME_TEXT_SIZE, GameConf.GAME_TEXT_COLOR, GameConf.GAME_TEXT_FONT, 1);
		this.scoreText = new Text(scene, this.op);
		this.highscoreText = new Text(scene, this.op);
		this.scoreText.setPos(new Vec(GameConf.PIXEL_W - 10, 10));
		this.highscoreText.setPos(new Vec(GameConf.PIXEL_W - 10, 50));
		this.highscore = this.getScene().getLevel().getHighScore();
	}

	@Override
	public void logicLoop() {
		this.score = this.getScene().getLevel().getScore();
		this.scoreText.setText(String.format("%d Points", this.score));
		this.highscoreText.setText(String.format("%d Highscore", this.highscore));
	}

	@Override
	public void internalRender(GameGrid f) {
		this.scoreText.internalRender(f);
		this.highscoreText.internalRender(f);
	}

}
