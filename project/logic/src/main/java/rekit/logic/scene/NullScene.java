package rekit.logic.scene;

import rekit.logic.GameModel;
import rekit.logic.gameelements.entities.Player;
import rekit.logic.gui.menu.MenuItem;

/**
 * This class realizes a Scene as a placeholder.
 */
final class NullScene extends Scene {
	/**
	 * Create a new NullScene.
	 *
	 * @param model
	 *            the model
	 */
	private NullScene(GameModel model) {
		super(model);
	}

	@Override
	public int getScore() {
		return 0;
	}

	@Override
	public int getHighScore() {
		return 0;
	}

	@Override
	public MenuItem getMenu() {
		return null;
	}

	@Override
	public Player getPlayer() {
		return null;
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
		return new NullScene(model);
	}

}
