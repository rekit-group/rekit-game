package rekit.logic.scene;

import rekit.logic.GameModel;
import rekit.logic.level.LevelFactory;

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
		super(model, LevelFactory.createBossRushLevel());
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
	public static Scene create(GameModel model, String... options) {
		return new BossRushScene(model);
	}

}
