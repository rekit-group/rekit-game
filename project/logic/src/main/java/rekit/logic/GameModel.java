package rekit.logic;

import rekit.config.GameConf;
import rekit.core.GameTime;
import rekit.logic.gameelements.GameElementFactory;
import rekit.logic.gameelements.entities.Entity;
import rekit.logic.gui.menu.MenuItem;
import rekit.logic.scene.Scenes;
import rekit.primitives.image.Filter;
import rekit.util.ThreadUtils;

/**
 * Main class of the Model. Manages the logic.
 *
 * @author Angelo Aracri
 * @author Dominik Fuchss
 *
 * @version 1.1
 */
public class GameModel implements Model {

	/**
	 * The current scene.
	 */
	private IScene curScene;
	/**
	 * Indicates the end of a game.
	 */
	private boolean endGame;
	/**
	 * The current state of the game.
	 */
	private GameState state;
	/**
	 * The current filter in the game.
	 */
	private Filter filter;
	/**
	 * Indicates a filter change.
	 */
	private boolean filterChange;

	/**
	 * Get a new model.
	 */
	public GameModel() {
		this.endGame = false;
		this.curScene = Scenes.NULL.getNewScene(this);
		GameElementFactory.initialize();
	}

	/**
	 * Init game.
	 */
	private void init() {
		this.switchScene(Scenes.MENU);
	}

	/**
	 * End game.
	 */
	private void end() {

	}

	@Override
	public void start() {
		this.init();
		ThreadUtils.runDaemon("GameModel", this::playGame);
	}

	/**
	 * This method will be invoked by the logic thread.
	 */
	private void playGame() {
		// repeat until player is dead
		while (!this.endGame) {
			this.logicLoop();
			ThreadUtils.sleep(GameConf.LOGIC_DELTA);
		}
		this.end();
	}

	/**
	 * Calculate DeltaTime Get Collisions .. and Invoke ReactCollision Iterate
	 * over Elements --&gt; invoke GameElement:logicLoop()
	 *
	 */
	public void logicLoop() {
		this.curScene.logicLoop();
	}

	/**
	 * Switch to scene with default parameters.
	 *
	 * @param s
	 *            the new scene.
	 */
	public void switchScene(Scenes s) {
		this.switchScene(s, new String[] {});
	}

	/**
	 * Switch to a different {@link Scenes}.# The new Scene will be initialized
	 * an started immediately. <br>
	 * Before you can switch to a newly created Scene you must create an entry
	 * in the {@link Scenes} enum.
	 *
	 * @param s
	 *            the scene to switch to
	 * @param options
	 *            pass options to the scene (e.g. the arcade level id)
	 */
	public void switchScene(Scenes s, String... options) {
		this.removeFilter();
		IScene nextScene = s.getNewScene(this, options);
		nextScene.init();
		nextScene.start();
		this.curScene.stop();
		this.curScene = nextScene;
		this.state = Scenes.getByInstance(this.curScene).isMenu() ? GameState.MENU : GameState.INGAME;
		GameTime.resume();
	}

	@Override
	public IScene getScene() {
		return this.curScene;
	}

	/**
	 * Return player.
	 *
	 * @return the player
	 */
	@Override
	public Entity getPlayer() {
		if (this.state != GameState.INGAME) {
			return null;
		}
		return this.curScene.getPlayer();
	}

	@Override
	public MenuItem getMenu() {
		return this.curScene.getMenu();
	}

	@Override
	public GameState getState() {
		if (this.state == GameState.INGAME || this.state == GameState.INGAME_PAUSED) {
			this.state = this.curScene.isPaused() ? GameState.INGAME_PAUSED : GameState.INGAME;
		}
		return this.state;
	}

	/**
	 * Synchronize objects for filters.
	 */
	private static final Object FILTER_SYNC = new Object();

	@Override
	public void setFilter(Filter f) {
		synchronized (GameModel.FILTER_SYNC) {
			this.filter = f;
			this.filterChange = true;
		}
	}

	@Override
	public void removeFilter() {
		synchronized (GameModel.FILTER_SYNC) {
			this.filter = null;
			this.filterChange = true;
		}
	}

	@Override
	public boolean filterChanged() {
		synchronized (GameModel.FILTER_SYNC) {
			return this.filterChange;
		}
	}

	@Override
	public Filter getFilter() {
		synchronized (GameModel.FILTER_SYNC) {
			this.filterChange = false;
			return this.filter;
		}
	}

}
