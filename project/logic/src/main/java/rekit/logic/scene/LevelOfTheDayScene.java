package rekit.logic.scene;

import rekit.logic.GameModel;
import rekit.logic.level.LevelFactory;
import rekit.persistence.level.LevelManager;

/**
 * This class realizes a LevelScene for lotd levels.
 */
final class LevelOfTheDayScene extends LevelScene {
	/**
	 * Create a new LOTD Scene.
	 *
	 * @param model
	 *            the model
	 */
	private LevelOfTheDayScene(GameModel model) {
		super(model, LevelFactory.createLevel(LevelManager.getLOTDLevel()));
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
		return new LevelOfTheDayScene(model);
	}

}
