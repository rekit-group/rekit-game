package edu.kit.informatik.ragnarok.logic.scene;

import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.level.LevelManager;

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
	public InfiniteLevelScene(GameModel model) {
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
