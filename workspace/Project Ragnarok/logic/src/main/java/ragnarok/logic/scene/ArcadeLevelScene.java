package ragnarok.logic.scene;

import ragnarok.logic.GameModel;
import ragnarok.logic.level.LevelManager;

/**
 * This class realizes a LevelScene for arcade levels.
 */
final class ArcadeLevelScene extends LevelScene {
	/**
	 * The arcade level's id.
	 */
	private final int arcadeLevelId;

	/**
	 * Create a new arcade level.
	 *
	 * @param model
	 *            the model
	 * @param arcadeLevelId
	 *            the arcade level's id
	 */
	private ArcadeLevelScene(GameModel model, int arcadeLevelId) {
		super(model, LevelManager.getArcadeLevel(arcadeLevelId));
		this.arcadeLevelId = arcadeLevelId;
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
		int arcadeId = Integer.parseInt(options[0]);
		return new ArcadeLevelScene(model, arcadeId);
	}

	/**
	 * Get the arcade level's id.
	 *
	 * @return the arcade level's id
	 */
	public int getArcadeId() {
		return this.arcadeLevelId;
	}

}
