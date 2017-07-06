package rekit.logic.scene;

import rekit.logic.GameModel;
import rekit.logic.level.LevelFactory;
import rekit.persistence.level.LevelManager;

public class TestScene extends LevelScene {

	public TestScene(GameModel model) {
		super(model, LevelFactory.createLevel(LevelManager.getTestLevel()));
	}

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
