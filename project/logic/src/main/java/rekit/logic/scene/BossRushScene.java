package rekit.logic.scene;

import rekit.logic.GameModel;
import rekit.logic.level.BossRushLevel;
import rekit.logic.level.Level;
import rekit.persistence.level.LevelManager;

/**
 * This class realizes a LevelScene for BossRush levels.
 */
final class BossRushScene extends LevelScene {
	/**
	 * Create a new BossRush Scene.
	 *
	 * @param model
	 *            the model
	 */
	private BossRushScene(GameModel model) {
		super(model, BossRushScene.getBossRushLevel());
	}

	private static Level getBossRushLevel() {
		BossRushLevel lv = new BossRushLevel();
		LevelManager.addLevel(lv.getDefinition());
		return lv;
	}

	/**
	 * Create method of the scene.
	 *
	 * @param model
	 *            the model
	 * @param options
	 *            the options
	 * @return a new arcade scene.
	 */
	public static Scene create(GameModel model, String[] options) {
		return new BossRushScene(model);
	}

}
