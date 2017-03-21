package rekit.logic.scene;

import rekit.logic.GameModel;
import rekit.persistence.level.LevelManager;

/**
 * This class realizes a LevelScene for infinite levels.
 */
final class InfiniteLevelScene extends LevelScene {
	/**
	 * Create a new scene.
	 *
	 * @param model
	 *            the model
	 */
	private InfiniteLevelScene(GameModel model) {
		super(model, LevelManager.getInfiniteLevel());
	}

	/**
	 * Create method of the scene.
	 *
	 * @param model
	 *            the model
	 * @param options
	 *            the options
	 * @return a new infinite scene.
	 */
	public static Scene create(GameModel model, String[] options) {
		return new InfiniteLevelScene(model);
	}

}
