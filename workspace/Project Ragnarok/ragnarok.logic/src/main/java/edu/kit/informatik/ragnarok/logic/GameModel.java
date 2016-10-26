package edu.kit.informatik.ragnarok.logic;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.core.CameraTarget;
import edu.kit.informatik.ragnarok.core.IScene;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Entity;
import edu.kit.informatik.ragnarok.logic.gui.menu.MenuItem;
import edu.kit.informatik.ragnarok.logic.scene.Scenes;
import edu.kit.informatik.ragnarok.primitives.image.Filter;
import edu.kit.informatik.ragnarok.util.ThreadUtils;

/**
 * Main class of the Model. Manages the logic
 *
 * @author Angelo Aracri
 * @author Dominik Fuchss
 *
 * @version 1.1
 */
public class GameModel implements CameraTarget, Model {

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
		ThreadUtils.runDaemon(() -> {
			// repeat until player is dead
			while (!this.endGame) {
				this.logicLoop();
				ThreadUtils.sleep(GameConf.LOGIC_DELTA);
			}
			this.end();
		});
	}

	/**
	 * Calculate DeltaTime Get Collisions .. & Invoke ReactCollision Iterate
	 * over Elements --> invoke GameElement:logicLoop()
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
		IScene nextScene = s.getNewScene(this, options);
		nextScene.init();
		nextScene.start();
		this.curScene.stop();
		this.curScene = nextScene;
		this.state = Scenes.getByInstance(this.curScene).isMenu() ? GameState.MENU : GameState.INGAME;
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
		if (this.state != GameState.MENU) {
			return null;
		}
		return this.curScene.getMenu();
	}

	@Override
	public float getCameraOffset() {
		return this.curScene.getCameraOffset();
	}

	@Override
	public GameState getState() {
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
