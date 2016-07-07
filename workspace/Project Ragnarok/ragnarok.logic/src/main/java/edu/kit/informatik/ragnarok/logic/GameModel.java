package edu.kit.informatik.ragnarok.logic;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.CameraTarget;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.gameelements.gui.menu.MenuItem;
import edu.kit.informatik.ragnarok.logic.save.LevelManager;
import edu.kit.informatik.ragnarok.logic.scene.LevelScene;
import edu.kit.informatik.ragnarok.logic.scene.MenuScene;
import edu.kit.informatik.ragnarok.logic.scene.NullScene;
import edu.kit.informatik.ragnarok.logic.scene.Scene;
import edu.kit.informatik.ragnarok.util.ThreadUtils;

/**
 * Main class of the Model. Manages the logic
 *
 * @author Angelo Aracri
 *
 * @version 1.1
 */
public class GameModel implements CameraTarget, Model {

	private long lastTime;
	private Scene curScene;
	private boolean endGame;
	private GameState state;

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

	public void switchScene(Scenes s) {
		int sceneId = s.id;
		Scene nextScene = null;

		// TODO: move scene creation to Enum
		switch (sceneId) {
		case 0:
			nextScene = new MenuScene(this);
			this.state = GameState.MENU;
			break;
		case 1:
			nextScene = new LevelScene(this, LevelManager.getInfiniteLevel());
			this.state = GameState.INGAME;
			break;
		case 2:
			nextScene = new LevelScene(this, LevelManager.getLOTDLevel());
			this.state = GameState.INGAME;
			break;

		default:
			throw new IllegalArgumentException("no scene with id " + sceneId);
		}

		nextScene.init();
		nextScene.start();
		this.curScene.stop();

		this.lastTime = 0;
		this.curScene = nextScene;

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

}
