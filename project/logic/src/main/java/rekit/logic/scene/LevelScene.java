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
import rekit.logic.ILevelScene;
import rekit.logic.filters.GrayScaleMode;
import rekit.logic.gameelements.GameElement;
import rekit.logic.gameelements.GameElementFactory;
import rekit.logic.gameelements.entities.Player;
import rekit.logic.gui.LifeGui;
import rekit.logic.gui.ScoreGui;
import rekit.logic.gui.Text;
import rekit.logic.gui.TimeDecorator;
import rekit.logic.gui.menu.MenuActionItem;
import rekit.logic.gui.menu.MenuItem;
import rekit.logic.gui.menu.MenuList;
import rekit.logic.gui.menu.SubMenu;
import rekit.logic.gui.parallax.HeapElementCloud;
import rekit.logic.gui.parallax.HeapElementMountain;
import rekit.logic.gui.parallax.HeapLayer;
import rekit.logic.gui.parallax.ParallaxContainer;
import rekit.logic.gui.parallax.TriangulationLayer;
import rekit.logic.level.Level;
import rekit.persistence.level.DataKey;
import rekit.persistence.level.DataKeySetter;
import rekit.persistence.level.LevelDefinition;
import rekit.persistence.level.SettingKey;
import rekit.primitives.TextOptions;
import rekit.primitives.geometry.Vec;
import rekit.primitives.time.Timer;
import rekit.util.CalcUtil;

/**
 * Scene that holds a playable Level created by a LevelCreator. Different Levels
 * are possible by changing the LevelCreator in the constructor.
 *
 * @author matze
 *
 */
public abstract class LevelScene extends Scene implements ILevelScene, DataKeySetter {

	/**
	 * Menu than will be displayed when the game is paused.
	 */
	protected SubMenu pauseMenu;

	/**
	 * Menu that will be displayed when the game has ended. Shows options
	 * dependent on whether the player won or lost and whether its an arcade
	 * level or not.
	 */
	protected SubMenu endMenu;

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
	private boolean ended = false;
	/**
	 * Indicates whether the level was successful (INFINITE || won).
	 */
	private boolean success = false;

	/**
	 * Create a new LevelScene.
	 *
	 * @param model
	 *            the model
	 * @param level
	 *            the leveldefinition
	 */
	public LevelScene(GameModel model, LevelDefinition level) {
		this(model, new Level(level));
	}

	/**
	 * Create a new LevelScene.
	 *
	 * @param model
	 *            the model
	 * @param level
	 *            the level
	 */
	public LevelScene(GameModel model, Level level) {
		super(model);
		this.level = level;
		this.ended = true;
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
		Text levelText = new Text(this, op).setText(this.level.getDefinition().getName());
		levelText.setPos(CalcUtil.units2pixel(new Vec(GameConf.GRID_W / 2f, GameConf.GRID_H / 2f)));
		this.addGuiElement(new TimeDecorator(this, levelText, new Timer(5000)));

		// create pause menu
		this.pauseMenu = new MenuList(this, "Pause Menu");
		this.pauseMenu.setPos(new Vec(GameConf.PIXEL_W / 2f, GameConf.PIXEL_H / 2f));

		MenuActionItem resume = new MenuActionItem(this, "Resume", () -> this.togglePause());
		MenuActionItem restart = new MenuActionItem(this, "Restart", () -> this.restart());
		MenuActionItem back = new MenuActionItem(this, "Main Menu", () -> this.getModel().switchScene(Scenes.MAIN_MENU));
		MenuActionItem desktop = new MenuActionItem(this, "Exit Game", () -> System.exit(0));

		this.pauseMenu.addItem(resume, restart, back, desktop);
		this.pauseMenu.setVisible(false);
		this.pauseMenu.select();
		this.addGuiElement(this.pauseMenu);

	}

	@Override
	public void start() {
		this.ended = false;
		this.getModel().removeFilter();
	}

	@Override
	public final void end(boolean won) {
		if (this.ended) {
			return;
		}
		this.ended = true;
		this.success = won || this.level.getDefinition().isSettingSet(SettingKey.INFINITE);

		// Update DataKeys
		DataKey.atEnd(this);

		// create the end menu before actually populating and showing it
		this.endMenu = new MenuList(this, "End Menu");
		this.endMenu.setPos(new Vec(GameConf.PIXEL_W / 2f, GameConf.PIXEL_H / 2f));
		this.endMenu.setVisible(false);
		this.addGuiElement(this.endMenu);

		int delay = this.performEndTasks(won);

		// show end menu after the specified time
		if (delay >= 0) {
			Timer.execute(delay, () -> this.showEndMenu(won));
		}
	}

	/**
	 * Populate the end menu and show it.
	 *
	 * @param won
	 *            Indicates whether the game was won. This has an effect on the
	 *            created items of the menu
	 */
	private void showEndMenu(boolean won) {
		// TODO definitely not the proper place to do this
		// do this in an FinitLevelScene and InifinitLevelScene (not to current
		// one a new one)
		MenuActionItem endBack;
		MenuActionItem endExit = new MenuActionItem(this, "Exit Game", () -> System.exit(0));

		if (!this.level.getDefinition().isSettingSet(SettingKey.INFINITE) && won) {

			String nextLevel = this.level.getNextLevel();
			if (nextLevel != null) {
				MenuActionItem endNext = new MenuActionItem(this, "Next Level", () -> this.getModel().switchScene(Scenes.ARCADE, nextLevel));
				this.endMenu.addItem(endNext);
			}

		} else {
			MenuActionItem endRestart = new MenuActionItem(this, "Restart", () -> this.restart());
			this.endMenu.addItem(endRestart);
		}

		if (!this.level.getDefinition().isSettingSet(SettingKey.INFINITE)) {
			// TODO go directly to level selection
			// maybe via an argument passed to MainMenueScene
			endBack = new MenuActionItem(this, "Level selection", () -> this.getModel().switchScene(Scenes.MAIN_MENU));
		} else {

			endBack = new MenuActionItem(this, "Main Menu", () -> this.getModel().switchScene(Scenes.MAIN_MENU));
		}

		this.endMenu.addItem(endBack, endExit);
		this.endMenu.select();
		this.endMenu.setVisible(true);
	}

	@Override
	public boolean hasEnded() {
		return this.ended;
	}

	/**
	 * Perform tasks on the end of the game (level).
	 *
	 * @param won
	 *            indicates whether successful or died
	 *
	 * @return delay delay (in ms) when to show the end menu. on -1 the endMenu
	 *         will not be shown.
	 *
	 */
	protected int performEndTasks(boolean won) {
		TextOptions op = new TextOptions(new Vec(-0.5f, -0.5f), 50, GameConf.GAME_TEXT_COLOR, GameConf.GAME_TEXT_FONT, Font.BOLD, false);

		Text levelText = new Text(this, op).setText("You" + (won ? " win!" : " have lost!"));
		levelText.setPos(CalcUtil.units2pixel(new Vec(GameConf.GRID_W / 2f, GameConf.GRID_H / 2f)));
		this.addGuiElement(new TimeDecorator(this, levelText, new Timer(2000)));
		if (won) {
			this.getModel().removeFilter();
		} else {
			this.getModel().setFilter(new GrayScaleMode());
		}
		// show menu short after the winning text has faded out
		return 2500;
	}

	@Override
	public void togglePause() {
		super.togglePause();
		// toggle visibility of pause menu
		this.pauseMenu.setVisible(!this.pauseMenu.isVisible());
		this.pauseMenu.setIndex(0);
	}

	@Override
	public void restart() {
		// reset all data structures
		this.init();
		// restart logic thread
		this.start();
	}

	@Override
	protected void logicLoopPre() {
		this.level.generate((int) (this.getCameraOffset() + GameConf.GRID_W + 1));

		// dont allow player to go behind currentOffset
		float minX = this.getCameraOffset() + this.player.getSize().x / 2f;
		if (this.player.getPos().x < minX) {
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
	public int getHighScore() {
		Integer hs = (Integer) this.level.getDefinition().getData(DataKey.HIGH_SCORE);
		return hs == null ? 0 : hs;
	}

	@Override
	public final MenuItem getMenu() {
		if (this.isPaused()) {
			return this.pauseMenu;
		}
		if (this.hasEnded()) {
			return this.endMenu;
		}
		return null;
	}

	@Override
	public boolean isLevelScene() {
		return true;
	}

	@Override
	public final LevelDefinition getDefinition() {
		return this.level.getDefinition();

	}

	@Override
	public final int getScore() {
		return (int) (this.player.getCameraOffset() + this.player.getPoints());
	}

	@Override
	public final boolean getSuccess() {
		return this.success;
	}
}
