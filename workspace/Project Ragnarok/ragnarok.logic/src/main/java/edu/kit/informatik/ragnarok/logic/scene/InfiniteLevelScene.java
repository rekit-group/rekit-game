package edu.kit.informatik.ragnarok.logic.scene;

import edu.kit.informatik.ragnarok.logic.GameModel;
import edu.kit.informatik.ragnarok.logic.level.LevelManager;

class InfiniteLevelScene extends LevelScene {

	public InfiniteLevelScene(GameModel model) {
		super(model, LevelManager.getInfiniteLevel());
	}

	public static Scene create(GameModel model, String[] options) {
		return new InfiniteLevelScene(model);
	}

}
