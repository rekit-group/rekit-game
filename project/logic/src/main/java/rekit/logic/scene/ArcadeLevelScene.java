package rekit.logic.scene;

import rekit.logic.GameModel;
import rekit.logic.level.LevelFactory;
import rekit.persistence.level.LevelManager;

/**
 * This class realizes a LevelScene for arcade levels.
 */
final class ArcadeLevelScene extends LevelScene {

	/**
	 * Create a new arcade level.
	 *
	 * @param model
	 *            the model
	 * @param arcadeLevelId
	 *            the arcade level's id
	 */
	private ArcadeLevelScene(GameModel model, String id) {
		super(model, LevelFactory.createLevel(LevelManager.getLevelById(id)));
	}

	/**
	 * Create method of the scene.
	 *
	 * @param model
	 *            the model
	 * @param options
	 *            the options - options[0] should be an Integer with the
	 *            arcadeLevelId
	 * @return a new arcade scene.
	 */
	public static Scene create(GameModel model, String[] options) {
		if (options == null || options.length < 1) {
			throw new IllegalArgumentException("cant switch to unspecified arcade level");
		}
		return new ArcadeLevelScene(model, options[0]);
	}

}
