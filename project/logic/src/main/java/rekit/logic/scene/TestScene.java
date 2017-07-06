package rekit.logic.scene;

import rekit.config.GameConf;
import rekit.logic.GameModel;
import rekit.logic.level.LevelFactory;
import rekit.persistence.level.LevelManager;

/**
 * A test scene which can be used in {@link GameConf#DEBUG} context.
 *
 * @author Matthias Schmitt
 *
 */
public final class TestScene extends LevelScene {

	private TestScene(GameModel model) {
		super(model, LevelFactory.createLevel(LevelManager.getTestLevel()));
	}

	/**
	 * Create the scene by model and options
	 * 
	 * @param model
	 *            the model
	 * @param options
	 *            the options
	 * @return the new scene
	 */
	public static Scene create(GameModel model, String... options) {
		return new TestScene(model);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void start() {
		super.start();
	}
}
