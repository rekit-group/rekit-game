package rekit.logic;

import java.util.concurrent.locks.ReentrantLock;

import rekit.config.GameConf;
import rekit.core.GameTime;
import rekit.logic.filters.Filter;
import rekit.logic.gameelements.GameElementFactory;
import rekit.logic.gameelements.entities.Entity;
import rekit.logic.gui.menu.MenuItem;
import rekit.logic.scene.Scenes;
import rekit.util.ThreadUtils;

/**
 * Main class of the Model. Manages the logic.
 *
 * @author Angelo Aracri
 * @author Dominik Fuchss
 */
public class GameModel implements Model {

	/**
	 * The current scene.
	 */
	private IScene scene;
	/**
	 * Indicates the end of a game.
	 */
	private boolean end;
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
		this.end = false;
		this.scene = Scenes.NULL.getNewScene(this);
		GameElementFactory.initialize();
	}

	/**
	 * End game.
	 */
	private void end() {

	}

	@Override
	public void start() {
		this.switchScene(Scenes.MAIN_MENU);
		ThreadUtils.runDaemon("GameModel", this::playGame);
	}

	/**
	 * This method will be invoked by the logic thread.
	 */
	private void playGame() {
		// repeat until player is dead
		while (!this.end) {
			long before = System.currentTimeMillis();
			this.scene.logicLoop();
			long after = System.currentTimeMillis();
			ThreadUtils.sleep(GameConf.LOGIC_DELTA - (after - before));
		}
		this.end();
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
		if (nextScene == null) {
			return;
		}
		this.removeFilter();
		nextScene.init();
		nextScene.start();
		this.scene = nextScene;
		this.state = Scenes.getByInstance(this.scene).isMenu() ? GameState.MENU : GameState.INGAME;
		GameTime.resume();
	}

	@Override
	public IScene getScene() {
		return this.scene;
	}

	/**
	 * Return player.
	 *
	 * @return the player
	 */
	@Override
	public Entity getPlayer() {
		if (!this.scene.isLevelScene()) {
			return null;
		}
		return ((ILevelScene) this.scene).getPlayer();
	}

	@Override
	public MenuItem getMenu() {
		return this.scene.getMenu();
	}

	@Override
	public GameState getState() {
		return this.state.calcState(this);
	}

	private final ReentrantLock filterLock = new ReentrantLock();

	@Override
	public void setFilter(Filter f) {
		try {
			this.filterLock.lock();
			this.filter = f;
			this.filterChange = true;
		} finally {
			this.filterLock.unlock();
		}

	}

	@Override
	public void removeFilter() {
		try {
			this.filterLock.lock();
			this.filter = null;
			this.filterChange = true;
		} finally {
			this.filterLock.unlock();
		}

	}

	@Override
	public boolean filterChanged() {
		try {
			this.filterLock.lock();
			return this.filterChange;
		} finally {
			this.filterLock.unlock();
		}

	}

	@Override
	public Filter getFilter() {
		try {
			this.filterLock.lock();
			this.filterChange = false;
			return this.filter;
		} finally {
			this.filterLock.unlock();
		}

	}

}
