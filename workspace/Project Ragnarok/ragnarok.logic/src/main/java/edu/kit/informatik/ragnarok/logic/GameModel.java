package edu.kit.informatik.ragnarok.logic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import edu.kit.informatik.ragnarok.config.GameConf;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.CameraTarget;
import edu.kit.informatik.ragnarok.logic.gameelements.entities.Player;
import edu.kit.informatik.ragnarok.logic.levelcreator.InfiniteLevelCreator;
import edu.kit.informatik.ragnarok.logic.scene.LevelScene;
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

	public long lastTime;

	private Thread loopThread;

	private Scene curScene;

	private boolean endGame;

	public GameModel() {
		this.init();
	}

	public void init() {
		this.switchScene(0);
	}

	private void end() {

	}

	@Override
	public void start() {
		this.loopThread = new Thread() {
			@Override
			public void run() {
				// repeat until player is dead
				while (!GameModel.this.endGame) {
					GameModel.this.logicLoop();
					ThreadUtils.sleep(GameConf.LOGIC_DELTA);
					// System.gc();
				}
				GameModel.this.end();
			}
		};
		this.loopThread.setDaemon(true);
		this.loopThread.start();
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

	public void switchScene(int scene) {
		switch (scene) {
		case 0:
			// curScene = new MainMenuScene();
			// break;

		case 1:
			InfiniteLevelCreator infiniteCreator = new InfiniteLevelCreator(new Random().nextInt());
			this.curScene = new LevelScene(this, infiniteCreator);
			break;

		case 2:
			DateFormat levelOfTheDayFormat = new SimpleDateFormat("ddMMyyyy");
			int seed = Integer.parseInt(levelOfTheDayFormat.format(Calendar.getInstance().getTime()));
			InfiniteLevelCreator levelOfTheDayCreator = new InfiniteLevelCreator(seed);
			this.curScene = new LevelScene(this, levelOfTheDayCreator);
			break;

		default:
			throw new IllegalArgumentException("no scene with id " + scene);
		}

		this.curScene.init();
		this.curScene.start();
		this.lastTime = 0;
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
	public GameState getState() {
		return GameState.INGAME;
	}
}
