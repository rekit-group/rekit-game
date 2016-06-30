package edu.kit.informatik.ragnarok.logic;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.CameraTarget;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.levelcreator.InfiniteLevelCreator;
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
	private List<SceneChangeListener> sceneChangeListeners;

	public GameModel() {
		this.endGame = false;
		this.sceneChangeListeners = new ArrayList<>();
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
			break;

		case 1:
			InfiniteLevelCreator infiniteCreator = new InfiniteLevelCreator(new SecureRandom().nextInt());
			nextScene = new LevelScene(this, infiniteCreator);
			break;

		case 2:
			DateFormat levelOfTheDayFormat = new SimpleDateFormat("ddMMyyyy");
			int seed = Integer.parseInt(levelOfTheDayFormat.format(Calendar.getInstance().getTime()));
			InfiniteLevelCreator levelOfTheDayCreator = new InfiniteLevelCreator(seed);
			nextScene = new LevelScene(this, levelOfTheDayCreator);
			break;

		default:
			throw new IllegalArgumentException("no scene with id " + sceneId);
		}

		nextScene.init();
		nextScene.start();
		this.curScene.stop();

		this.lastTime = 0;
		this.curScene = nextScene;

		this.updateSceneChangeListeners();
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
		return this.curScene.getPlayer();
	}

	@Override
	public float getCameraOffset() {
		return this.curScene.getCameraOffset();
	}

	@Override
	public void registerSceneChangeListener(SceneChangeListener l) {
		this.sceneChangeListeners.add(l);
	}

	public void updateSceneChangeListeners() {
		for (SceneChangeListener sceneChangeListener : this.sceneChangeListeners) {
			sceneChangeListener.sceneChanged(this.curScene);
		}
	}

}
