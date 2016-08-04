package edu.kit.informatik.ragnarok.logic;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.CameraTarget;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gui.menu.MenuItem;
import edu.kit.informatik.ragnarok.logic.scene.MenuScene;
import edu.kit.informatik.ragnarok.logic.scene.NullScene;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.logic.scene.Scenes;
import edu.kit.informatik.ragnarok.primitives.image.Filter;
import edu.kit.informatik.ragnarok.util.ThreadUtils;

/**
 * Main class of the Model. Manages the logic
 *
 * @author Angelo Aracri
 * @author Dominik Fuchß
 *
 * @version 1.1
 */
public class GameModel implements CameraTarget, Model {

	private long lastTime;
	private Scene curScene;
	private boolean endGame;
	private GameState state;

	private Filter filter;
	private boolean filterChange;

	public GameModel() {
		this.endGame = false;
		this.curScene = new NullScene(this);
	}

	private void init() {
		this.switchScene(Scenes.MENU);
	}

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

		// calculate time difference since last physics loop
		long timeNow = System.currentTimeMillis();
		if (this.lastTime == 0) {
			this.lastTime = System.currentTimeMillis();
		}
		long timeDelta = timeNow - this.lastTime;

		this.curScene.logicLoop(timeDelta / 1000.f);

		// update time
		this.lastTime = timeNow;

	}

	/**
	 * {@link #switchScene(Scenes, String[])} with null as options
	 *
	 * @param s
	 */
	public void switchScene(Scenes s) {
		this.switchScene(s, null);
	}

	/**
	 * Switch to a different {@link Scene}. The new Scene will be initialized an
	 * started immediately. </br>
	 * Before you can switch to a newly created Scene you must create an entry
	 * in the {@link Scenes} enum.
	 *
	 * @param s
	 *            the scene to switch to
	 * @param options
	 *            pass options to the scene (e.g. the arcade level id)
	 */
	public void switchScene(Scenes s, String[] options) {
		Scene nextScene = s.getNewScene(this, options);
		nextScene.init();
		nextScene.start();
		this.curScene.stop();
		this.lastTime = 0;
		this.curScene = nextScene;
		this.state = Scenes.getByInstance(this.curScene).isMenu() ? GameState.MENU : GameState.INGAME;
	}

	@Override
	public Scene getScene() {
		return this.curScene;
	}

	/**
	 * Return player
	 *
	 * @return the player
	 */
	@Override
	public Player getPlayer() {
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
		return ((MenuScene) this.curScene).getMenu();
	}

	@Override
	public float getCameraOffset() {
		return this.curScene.getCameraOffset();
	}

	@Override
	public GameState getState() {
		return this.state;
	}

	public long getTime() {
		return this.lastTime;
	}

	/**
	 * Synchronize filters
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
