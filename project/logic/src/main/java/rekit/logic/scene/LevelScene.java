/**
 *
 */
package rekit.logic.scene;

import java.awt.Font;
import java.util.HashSet;
import java.util.Set;

import rekit.config.GameConf;
import rekit.core.CameraTarget;
import rekit.logic.GameModel;
import rekit.logic.filters.GrayScaleMode;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.GameElementFactory;
import rekit.logic.gameelements.entities.Player;
import rekit.logic.gui.LifeGui;
import rekit.logic.gui.ScoreGui;
import rekit.logic.gui.Text;
import rekit.logic.gui.TimeDecorator;
import rekit.logic.gui.menu.MenuItem;
import rekit.logic.gui.parallax.HeapElementCloud;
import rekit.logic.gui.parallax.HeapElementMountain;
import rekit.logic.gui.parallax.HeapLayer;
import rekit.logic.gui.parallax.ParallaxContainer;
import rekit.logic.gui.parallax.TriangulationLayer;
import rekit.logic.level.Level;
import rekit.persistence.level.DataKey;
import rekit.persistence.level.LevelDefinition;
import rekit.persistence.level.LevelManager;
import rekit.primitives.geometry.Vec;
import rekit.primitives.time.Timer;
import rekit.util.CalcUtil;
import rekit.util.TextOptions;

/**
 * Scene that holds a playable Level created by a LevelCreator. Different Levels
 * are possible by changing the LevelCreator in the constructor.
 *
 * @author matze
 *
 */
public abstract class LevelScene extends Scene {
	/**
	 * The player in the scene.
	 */
	private Player player = new Player(new Vec(6, 5));
	/**
	 * The level in the scene.
	 */
	private Level level;
	/**
	 * The current camera target.
	 */
	private CameraTarget cameraTarget;
	/**
	 * The score gui element.
	 */
	private ScoreGui scoreGui;
	/**
	 * The life gui element.
	 */
	private LifeGui lifeGui;
	/**
	 * The ParallaxContainer for the background.
	 */
	protected ParallaxContainer parallax;
	/**
	 * Indicates whether the level has ended.
	 */
	private boolean hasEnded;

	/**
	 * Create a new LevelScene.
	 *
	 * @param model
	 *            the model
	 * @param level
	 *            the level
	 */
	public LevelScene(GameModel model, LevelDefinition level) {
		super(model);
		this.level = new Level(level);
		this.hasEnded = true;
	}

	@Override
	public void init() {
		super.init();

		// Create Player and add him to game
		this.player.init();
		this.cameraTarget = this.player;
		this.addGameElement(this.player);

		// Init EnemyFactory with model
		GameElementFactory.setScene(this);
		this.level.reset();
		// Create parallax background
		this.parallax = new ParallaxContainer(this);

		this.parallax.addLayer(new TriangulationLayer(1.5f));
		this.parallax.addLayer(new HeapLayer(new HeapElementCloud(null, new Vec(), null, null), 1.1f));
		this.parallax.addLayer(new HeapLayer(new HeapElementMountain(null, new Vec(), null, null), 1.3f));

		// Create Gui
		this.scoreGui = new ScoreGui(this);
		this.scoreGui.setPos(new Vec(10, 10));
		this.lifeGui = new LifeGui(this);
		this.lifeGui.setPos(new Vec(10));
		this.addGuiElement(this.scoreGui);
		this.addGuiElement(this.lifeGui);

		TextOptions op = new TextOptions(new Vec(-0.5f, -0.5f), 40, GameConf.GAME_TEXT_COLOR, GameConf.GAME_TEXT_FONT, Font.BOLD);
		Text levelText = new Text(this, op).setText(this.level.getName());
		levelText.setPos(CalcUtil.units2pixel(new Vec(GameConf.GRID_W / 2f, GameConf.GRID_H / 2f)));
		this.addGuiElement(new TimeDecorator(this, levelText, new Timer(5000)));

	}

	@Override
	public void start() {
		this.hasEnded = false;
	}

	@Override
	public final void end(boolean won) {
		if (this.hasEnded) {
			return;
		}
		this.hasEnded = true;
		this.performEndTasks(won);
		// only save score if the level is infinite or the player has won
		// don't save it upon losing in finite level
		if (this.level.getDefinition().isSettingSet("infinite") || won) {
			// save score if higher than highscore
			if (this.getScore() > this.getHighScore()) {
				this.setHighScore(this.getScore());
			}
		}
		Timer.execute(6000, () -> this.getModel().switchScene(Scenes.MENU));
	}

	/**
	 * Perform tasks on the end of the game (level).
	 *
	 * @param won
	 *            indicates whether successful or died
	 */
	protected void performEndTasks(boolean won) {
		TextOptions op = new TextOptions(new Vec(-0.5f, -0.5f), 50, GameConf.GAME_TEXT_COLOR, GameConf.GAME_TEXT_FONT, Font.BOLD, false);
		Text levelText = new Text(this, op).setText("You" + (won ? " win!" : " have lost!"));
		levelText.setPos(CalcUtil.units2pixel(new Vec(GameConf.GRID_W / 2f, GameConf.GRID_H / 2f)));
		this.addGuiElement(new TimeDecorator(this, levelText, new Timer(5000)));
		if (won) {
			this.getModel().removeFilter();
		} else {
			this.getModel().setFilter(new GrayScaleMode());
		}
	}

	@Override
	public void restart() {
		// wait 2 seconds
		Timer.sleep(2000);
		// reset all data structures
		this.init();
		// restart logic thread
		this.start();
	}

	@Override
	protected void logicLoopPre() {
		this.level.generate((int) (this.getCameraOffset() + GameConf.GRID_W + 1));

		// dont allow player to go behind currentOffset
		float minX = this.getCameraOffset() + this.player.getSize().getX() / 2f;
		if (this.player.getPos().getX() < minX) {
			this.player.setPos(this.player.getPos().setX(minX));
		}

		this.parallax.logicLoop(this.getCameraOffset());

	}

	@Override
	protected void logicLoopAfter() {
		if (this.isPaused()) {
			return;
		}

		this.checkCollisions();
		if (this.player.getDeleteMe()) {
			this.end(false);
		}
	}

	/**
	 * Check and Threat collisions.
	 */
	private void checkCollisions() {
		Set<GameElement> elements = new HashSet<>();
		this.applyToNonNeutralGameElements(elements::add);
		elements.forEach(e1 -> elements.forEach(e1::checkCollision));
	}

	@Override
	public Player getPlayer() {
		return this.player;
	}

	@Override
	public float getCameraOffset() {
		return this.cameraTarget.getCameraOffset();
	}

	@Override
	public void setCameraTarget(CameraTarget cameraTarget) {
		this.cameraTarget = cameraTarget;
	}

	@Override
	public int getScore() {
		return (int) (this.player.getCameraOffset() + this.getPlayer().getPoints());
	}

	@Override
	public int getHighScore() {
		Integer hs = (Integer) this.level.getDefinition().getData(DataKey.HIGH_SCORE);
		if (hs == null) {
			return 0;
		}
		return hs;
	}

	/**
	 * Set the highscore of the level.
	 *
	 * @param highScore
	 *            the highscore
	 */
	public void setHighScore(int highScore) {
		this.level.getDefinition().setData(DataKey.HIGH_SCORE, highScore);
		LevelManager.contentChanged();
	}

	@Override
	public final MenuItem getMenu() {
		throw new UnsupportedOperationException("Menu not supported in LevelScene");
	}
}
